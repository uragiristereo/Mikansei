package com.uragiristereo.mejiboard.feature.home.posts

import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.uragiristereo.mejiboard.core.common.data.Constants
import com.uragiristereo.mejiboard.core.database.dao.session.SessionDao
import com.uragiristereo.mejiboard.core.database.dao.session.toPostList
import com.uragiristereo.mejiboard.core.database.dao.session.toPostSessionList
import com.uragiristereo.mejiboard.core.model.booru.BooruSource
import com.uragiristereo.mejiboard.core.model.booru.post.Post
import com.uragiristereo.mejiboard.domain.usecase.GetPostsUseCase
import com.uragiristereo.mejiboard.feature.home.posts.state.PostsContentState
import com.uragiristereo.mejiboard.feature.home.posts.state.PostsLoadingState
import com.uragiristereo.mejiboard.feature.home.posts.state.PostsSavedState
import com.uragiristereo.mejiboard.lib.navigation_extension.getData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.UUID

@OptIn(SavedStateHandleSaveableApi::class)
class PostsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getPostsUseCase: GetPostsUseCase,
    private val sessionDao: SessionDao,
) : ViewModel() {
    val tags = savedStateHandle.getData<PostsRoute.IndexRoute>()?.tags ?: ""

    var topAppBarHeight by mutableStateOf(0.dp)
    val offsetY = Animatable(initialValue = 0f)
    var lastFabVisible by mutableStateOf(false)
    var dialogShown by mutableStateOf(false)

    // posts
    var posts by mutableStateOf<List<Post>>(listOf())
        private set

    private var page by savedStateHandle.saveable { mutableStateOf(0) }

    var loading by mutableStateOf(PostsLoadingState.FROM_LOAD)

    var canLoadMore by savedStateHandle.saveable { mutableStateOf(false) }
        private set

    var errorMessage by mutableStateOf<String?>(null)
    var selectedBooru by mutableStateOf<BooruSource?>(savedStateHandle[Constants.STATE_KEY_POSTS_SELECTED_BOORU])

    val contentState by derivedStateOf {
        val loadingDisabled = loading == PostsLoadingState.DISABLED || loading == PostsLoadingState.DISABLED_REFRESHED

        when {
            loadingDisabled && posts.isNotEmpty() -> PostsContentState.SHOW_POSTS
            loadingDisabled && posts.isEmpty() && errorMessage == null -> PostsContentState.SHOW_EMPTY
            loadingDisabled && errorMessage != null -> PostsContentState.SHOW_ERROR
            loading == PostsLoadingState.FROM_LOAD -> PostsContentState.SHOW_MAIN_LOADING
            loading == PostsLoadingState.FROM_RESTORE_SESSION -> PostsContentState.SHOW_NOTHING
            else -> PostsContentState.SHOW_POSTS
        }
    }

    // post
    var selectedPost by mutableStateOf<Post?>(null)

    // session
    private var postsSession = listOf<Post>()

    var savedState by savedStateHandle.saveable {
        mutableStateOf(PostsSavedState())
    }
        private set

    var jumpToPosition by mutableStateOf(false)

    private var initialized = false
    private var sessionId = savedStateHandle[Constants.STATE_KEY_POSTS_SESSION_ID] ?: UUID.randomUUID().toString()
    private var postsJob: Job? = null

    init {
        if (!initialized) {
            initialized = true
            savedStateHandle[Constants.STATE_KEY_POSTS_SESSION_ID] = sessionId

            when {
                savedState.loadFromSession -> getPostsFromSession()
                else -> getPosts()
            }
        }
    }

    fun getPosts(refresh: Boolean = true) {
        postsJob?.cancel()

        postsJob = viewModelScope.launch {
            when {
                refresh -> page = 0
                else -> page += 1
            }

            errorMessage = null

            getPostsUseCase(
                tags = tags,
                page = page,
                currentPosts = posts,
                onStart = { booru ->
                    selectedBooru = booru
                    savedStateHandle[Constants.STATE_KEY_POSTS_SELECTED_BOORU] = selectedBooru
                },
                onLoading = { isLoading ->
                    loading = when {
                        isLoading && (posts.isNotEmpty() && refresh) -> PostsLoadingState.FROM_REFRESH
                        isLoading && (posts.isNotEmpty() && page > 0) -> PostsLoadingState.FROM_LOAD_MORE
                        !isLoading && (posts.isNotEmpty() && refresh) -> PostsLoadingState.DISABLED_REFRESHED
                        isLoading -> PostsLoadingState.FROM_LOAD
                        else -> PostsLoadingState.DISABLED
                    }
                },
                onSuccess = { data, canLoadMoreResult: Boolean ->
                    posts = data

                    errorMessage = null
                    canLoadMore = canLoadMoreResult
                },
                onFailed = { message ->
                    errorMessage = message
                    Timber.d(errorMessage)
                },
                onError = { t ->
                    errorMessage = t.toString()
                    Timber.d(errorMessage)
                }

            )
        }
    }

    fun retryGetPosts() {
        getPosts(refresh = true)
    }

    private fun getPostsFromSession() {
        viewModelScope.launch {
            loading = PostsLoadingState.FROM_RESTORE_SESSION
            savedState = savedState.copy(loadFromSession = false)

            val postsSession: List<Post>

            withContext(Dispatchers.IO) {
                postsSession = sessionDao.getPosts(sessionId = sessionId).toPostList()
            }

            if (postsSession.isNotEmpty()) {
                posts = postsSession

                jumpToPosition = true
                loading = PostsLoadingState.DISABLED
            } else {
                getPosts(refresh = true)
            }
        }
    }

    fun updatePostsSession() {
        viewModelScope.launch(Dispatchers.IO) {
            val postsAsList = posts.toList()

            if (postsAsList != postsSession) {
                postsSession = postsAsList

                sessionDao.deleteSession(sessionId)

                sessionDao.insertSession(postsAsList.toPostSessionList(sessionId))
            }
        }
    }

    fun updateSessionPosition(index: Int, offset: Int) {
        savedState = savedState.copy(
            loadFromSession = true,
            scrollIndex = index,
            scrollOffset = offset,
        )
    }
}
