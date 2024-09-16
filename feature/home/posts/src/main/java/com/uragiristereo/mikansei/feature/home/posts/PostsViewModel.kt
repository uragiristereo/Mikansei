package com.uragiristereo.mikansei.feature.home.posts

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Profile
import com.uragiristereo.mikansei.core.domain.module.database.SessionRepository
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.domain.module.database.entity.Session
import com.uragiristereo.mikansei.core.domain.usecase.GetPostsUseCase
import com.uragiristereo.mikansei.core.model.Constants
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.ui.entity.ImmutableList
import com.uragiristereo.mikansei.core.ui.entity.immutableListOf
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.feature.home.posts.state.PostsContentState
import com.uragiristereo.mikansei.feature.home.posts.state.PostsLoadingState
import com.uragiristereo.serializednavigationextension.runtime.navArgsOf
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.UUID

@OptIn(SavedStateHandleSaveableApi::class)
class PostsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository,
    private val getPostsUseCase: GetPostsUseCase,
) : ViewModel() {
    val tags = savedStateHandle.navArgsOf<HomeRoute.Posts>()?.tags ?: ""

    private val channel = Channel<Event>()
    val event = channel.receiveAsFlow()

    var topAppBarHeight by mutableStateOf(0.dp)
    val offsetY = Animatable(initialValue = 0f)

    // posts
    var posts by mutableStateOf<ImmutableList<Post>>(immutableListOf())
        private set

    private var page by savedStateHandle.saveable { mutableIntStateOf(1) }

    var loading by mutableStateOf(PostsLoadingState.FROM_LOAD)

    var canLoadMore by savedStateHandle.saveable { mutableStateOf(false) }
        private set

    var errorMessage by mutableStateOf<String?>(null)

    private var firstLoad by mutableStateOf(true)

    val contentState by derivedStateOf {
        val loadingDisabled =
            loading == PostsLoadingState.DISABLED || loading == PostsLoadingState.DISABLED_REFRESHED

        when {
            loadingDisabled && posts.value.isNotEmpty() -> PostsContentState.SHOW_POSTS
            loadingDisabled && (posts.value.isEmpty() && !firstLoad) && errorMessage == null -> PostsContentState.SHOW_EMPTY
            loadingDisabled && errorMessage != null -> PostsContentState.SHOW_ERROR
            loading == PostsLoadingState.FROM_LOAD -> PostsContentState.SHOW_MAIN_LOADING
            loading == PostsLoadingState.FROM_RESTORE_SESSION -> PostsContentState.SHOW_NOTHING
            else -> PostsContentState.SHOW_POSTS
        }
    }

    private var postsJob: Job? = null
    val gridState = LazyStaggeredGridState()

    // session
    private val sessionId = savedStateHandle.get<String>(Constants.STATE_KEY_POSTS_SESSION_ID).run {
        this ?: UUID.randomUUID().toString()
    }

    private val session = sessionRepository.getSession(sessionId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null,
        )

    private val isLoadFromSession = savedStateHandle["IS_LOAD_FROM_SESSION"] ?: false

    // saved searches
    val activeUser: StateFlow<Profile>
        get() = userRepository.active

    private var shouldRetryGettingPosts = true

    init {
        savedStateHandle[Constants.STATE_KEY_POSTS_SESSION_ID] = sessionId

        viewModelScope.launch {
            if (isLoadFromSession) {
                getPostsFromSession()
            } else {
                sessionRepository.updateSession(Session(sessionId, tags))
                getPosts()
            }
        }

        viewModelScope.launch {
            sessionRepository.getPosts(sessionId).collect {
                posts = immutableListOf(it)
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
                sessionId = sessionId,
                tags = tags,
                page = page,
            )

            when (result) {
                is Result.Success -> {
                    val isSavedSearchesQuerying = result.data.isEmpty
                            && tags.contains("search:")
                            && refresh
                            && shouldRetryGettingPosts

                    if (isSavedSearchesQuerying) {
                        delay(timeMillis = 500L)
                        shouldRetryGettingPosts = false
                        getPosts(refresh = true)
                    } else {
                        canLoadMore = result.data.canLoadMore
                        errorMessage = null
                        shouldRetryGettingPosts = false
                        firstLoad = false

                        disableLoadingState(refresh)
                    }
                }

                is Result.Failed -> {
                    errorMessage = result.message
                    firstLoad = false
                    disableLoadingState(refresh)

                    Timber.d(errorMessage)
                }

                is Result.Error -> {
                    if (result.t !is CancellationException) {
                        errorMessage = result.t.toString()
                        firstLoad = false
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

    private suspend fun getPostsFromSession() {
        loading = PostsLoadingState.FROM_RESTORE_SESSION
        val currentSession = session.filterNotNull().first()
        // make sure the session is actually loaded before jumping to position
        sessionRepository.getPosts(sessionId).first()
        loading = PostsLoadingState.DISABLED
        channel.send(Event.OnJumpToPosition(currentSession))
    }

    fun updateSessionPosition(index: Int, offset: Int) {
        session.value?.let {
            viewModelScope.launch {
                savedStateHandle["IS_LOAD_FROM_SESSION"] = true
                sessionRepository.updateSession(
                    it.copy(scrollIndex = index, scrollOffset = offset)
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()

        viewModelScope.launch(SupervisorJob()) {
            sessionRepository.delete(sessionId)
        }
    }

    sealed interface Event {
        data class OnJumpToPosition(val session: Session) : Event
    }
}
