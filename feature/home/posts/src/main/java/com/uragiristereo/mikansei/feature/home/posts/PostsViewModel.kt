package com.uragiristereo.mikansei.feature.home.posts

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
import com.uragiristereo.mikansei.core.database.session.SessionDao
import com.uragiristereo.mikansei.core.database.session.toSessionList
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Profile
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.domain.usecase.GetPostsUseCase
import com.uragiristereo.mikansei.core.model.Constants
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.ui.entity.ImmutableList
import com.uragiristereo.mikansei.core.ui.entity.immutableListOf
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.feature.home.posts.state.PostsContentState
import com.uragiristereo.mikansei.feature.home.posts.state.PostsLoadingState
import com.uragiristereo.mikansei.feature.home.posts.state.PostsSavedState
import com.uragiristereo.serializednavigationextension.runtime.navArgsOf
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.UUID

@OptIn(SavedStateHandleSaveableApi::class)
class PostsViewModel(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val getPostsUseCase: GetPostsUseCase,
    private val sessionDao: SessionDao,
) : ViewModel() {
    val tags = savedStateHandle.navArgsOf<HomeRoute.Posts>()?.tags ?: ""

    var topAppBarHeight by mutableStateOf(0.dp)
    val offsetY = Animatable(initialValue = 0f)

    // posts
    var posts by mutableStateOf<ImmutableList<Post>>(immutableListOf())
        private set

    private var page by savedStateHandle.saveable { mutableStateOf(1) }

    var loading by mutableStateOf(PostsLoadingState.FROM_LOAD)

    var canLoadMore by savedStateHandle.saveable { mutableStateOf(false) }
        private set

    var errorMessage by mutableStateOf<String?>(null)

    val contentState by derivedStateOf {
        val loadingDisabled = loading == PostsLoadingState.DISABLED || loading == PostsLoadingState.DISABLED_REFRESHED

        when {
            loadingDisabled && posts.value.isNotEmpty() -> PostsContentState.SHOW_POSTS
            loadingDisabled && posts.value.isEmpty() && errorMessage == null -> PostsContentState.SHOW_EMPTY
            loadingDisabled && errorMessage != null -> PostsContentState.SHOW_ERROR
            loading == PostsLoadingState.FROM_LOAD -> PostsContentState.SHOW_MAIN_LOADING
            loading == PostsLoadingState.FROM_RESTORE_SESSION -> PostsContentState.SHOW_NOTHING
            else -> PostsContentState.SHOW_POSTS
        }
    }

    // session
    private var postsSession = listOf<Post>()

    var savedState by savedStateHandle.saveable {
        mutableStateOf(PostsSavedState())
    }
        private set

    var jumpToPosition by mutableStateOf(false)

    private var initialized = false
    private var sessionUuid = savedStateHandle[Constants.STATE_KEY_POSTS_SESSION_ID] ?: UUID.randomUUID().toString()
    private var postsJob: Job? = null

    // saved searches
    val activeUser: StateFlow<Profile>
        get() = userRepository.active

    private var shouldRetryGettingPosts = true

    init {
        if (!initialized) {
            initialized = true
            savedStateHandle[Constants.STATE_KEY_POSTS_SESSION_ID] = sessionUuid

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
                refresh -> page = 1
                else -> page += 1
            }

            loading = when {
                posts.value.isNotEmpty() && refresh -> PostsLoadingState.FROM_REFRESH
                posts.value.isNotEmpty() && page > 1 -> PostsLoadingState.FROM_LOAD_MORE
                else -> PostsLoadingState.FROM_LOAD
            }

            errorMessage = null

            val result = getPostsUseCase(
                tags = tags,
                page = page,
                currentPosts = posts.value,
            )

            when (result) {
                is Result.Success -> {
                    val isSavedSearchesQuerying = result.data.posts.isEmpty()
                            && tags.contains("search:")
                            && refresh
                            && shouldRetryGettingPosts

                    if (isSavedSearchesQuerying) {
                        delay(timeMillis = 500L)
                        shouldRetryGettingPosts = false
                        getPosts(refresh = true)
                    } else {
                        posts = immutableListOf(result.data.posts)
                        canLoadMore = result.data.canLoadMore
                        errorMessage = null
                        shouldRetryGettingPosts = false

                        disableLoadingState(refresh)
                    }
                }

                is Result.Failed -> {
                    errorMessage = result.message
                    disableLoadingState(refresh)

                    Timber.d(errorMessage)
                }

                is Result.Error -> {
                    if (result.t !is CancellationException) {
                        errorMessage = result.t.toString()
                        disableLoadingState(refresh)

                        Timber.d(errorMessage)
                    }
                }
            }
        }
    }

    private fun disableLoadingState(refresh: Boolean) {
        loading = when {
            posts.value.isNotEmpty() && refresh -> PostsLoadingState.DISABLED_REFRESHED
            else -> PostsLoadingState.DISABLED
        }
    }

    fun retryGetPosts() {
        getPosts(refresh = true)
    }

    private fun getPostsFromSession() {
        viewModelScope.launch {
            loading = PostsLoadingState.FROM_RESTORE_SESSION
            savedState = savedState.copy(loadFromSession = false)

            val postsSession = withContext(Dispatchers.IO) {
                sessionDao.get(sessionUuid).map { it.post }
            }

            when {
                postsSession.isNotEmpty() -> {
                    posts = immutableListOf(postsSession)

                    jumpToPosition = true
                    loading = PostsLoadingState.DISABLED
                }

                else -> getPosts(refresh = true)
            }
        }
    }

    fun updatePostsSession() {
        viewModelScope.launch(Dispatchers.IO) {
            val postsAsList = posts.value.toList()

            if (postsAsList != postsSession) {
                postsSession = postsAsList

                sessionDao.delete(sessionUuid)

                sessionDao.insert(postsAsList.toSessionList(sessionUuid))
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
