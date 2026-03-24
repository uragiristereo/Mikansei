package com.uragiristereo.mikansei.feature.home.posts

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Profile
import com.uragiristereo.mikansei.core.domain.module.database.SessionRepository
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.domain.module.database.entity.Session
import com.uragiristereo.mikansei.core.domain.usecase.FilterPostsUseCase
import com.uragiristereo.mikansei.core.domain.usecase.GetPostsUseCase
import com.uragiristereo.mikansei.core.model.Constants
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.ui.entity.ImmutableList
import com.uragiristereo.mikansei.core.ui.entity.immutableListOf
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.feature.home.posts.state.PostsContentState
import com.uragiristereo.mikansei.feature.home.posts.state.PostsLoadingState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.UUID

class PostsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository,
    private val getPostsUseCase: GetPostsUseCase,
    private val filterPostsUseCase: FilterPostsUseCase,
) : ViewModel() {
    val tags = savedStateHandle.toRoute<HomeRoute.Posts>().tags

    private val channel = Channel<Event>()
    val event = channel.receiveAsFlow()

    var topAppBarHeight by mutableStateOf(0.dp)
    val offsetY = Animatable(initialValue = 0f)

    // session
    val sessionId = savedStateHandle.get<String>(Constants.STATE_KEY_POSTS_SESSION_ID).run {
        this ?: UUID.randomUUID().toString()
    }

    val session = sessionRepository.getSession(sessionId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null,
        )

    private val isLoadFromSession = savedStateHandle[STATE_KEY_IS_LOAD_FROM_SESSION] ?: false

    // posts
    var posts by mutableStateOf<ImmutableList<Post>>(immutableListOf())

    val filteredPosts = combine(
        sessionRepository.getPosts(sessionId),
        userRepository.active,
    ) { sessionPosts, _ ->
        filterPostsUseCase(sessionPosts, tags)
    }.map { filteredPosts ->
        immutableListOf(filteredPosts).also { filteredPosts ->
            if (firstLoadFromSession) {
                firstLoadFromSession = false
                posts = filteredPosts
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = immutableListOf(),
    )

    var loading by mutableStateOf(PostsLoadingState.FROM_LOAD)

    var errorMessage by mutableStateOf<String?>(null)

    private var firstLoad by mutableStateOf(true)
    private var firstLoadFromSession by mutableStateOf(true)

    val contentState by derivedStateOf {
        val loadingDisabled =
            loading == PostsLoadingState.DISABLED || loading == PostsLoadingState.DISABLED_REFRESHED

        when {
            loadingDisabled && posts.value.isNotEmpty() -> PostsContentState.SHOW_POSTS
            loadingDisabled && posts.value.isEmpty() && errorMessage == null && firstLoad -> PostsContentState.SHOW_MAIN_LOADING
            loadingDisabled && posts.value.isEmpty() && errorMessage == null -> PostsContentState.SHOW_EMPTY
            loadingDisabled && errorMessage != null -> PostsContentState.SHOW_ERROR
            loading == PostsLoadingState.FROM_LOAD -> PostsContentState.SHOW_MAIN_LOADING
            loading == PostsLoadingState.FROM_RESTORE_SESSION -> PostsContentState.SHOW_NOTHING
            else -> PostsContentState.SHOW_POSTS
        }
    }

    private var postsJob: Job? = null
    val gridState = LazyStaggeredGridState()

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
    }

    fun getPosts(refresh: Boolean = true) {
        postsJob?.cancel()

        postsJob = viewModelScope.launch {
            val page = session.value?.page ?: 1
            val newPage = when {
                refresh -> 1
                else -> page + 1
            }

            loading = when {
                posts.value.isNotEmpty() && refresh -> PostsLoadingState.FROM_REFRESH
                posts.value.isNotEmpty() && newPage > 1 -> PostsLoadingState.FROM_LOAD_MORE
                else -> PostsLoadingState.FROM_LOAD
            }

            errorMessage = null

            val result = getPostsUseCase(
                sessionId = sessionId,
                tags = tags,
                page = newPage,
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
                        updateSessionResult(
                            page = newPage,
                            canLoadMore = result.data.canLoadMore,
                        )
                        errorMessage = null
                        shouldRetryGettingPosts = false

                        if (result.data.isEmpty) {
                            firstLoad = false
                        }

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
        viewModelScope.launch {
            val session = session.value ?: return@launch
            savedStateHandle[STATE_KEY_IS_LOAD_FROM_SESSION] = true
            sessionRepository.updateSession(
                session.copy(scrollIndex = index, scrollOffset = offset)
            )
        }
    }

    private suspend fun updateSessionResult(page: Int, canLoadMore: Boolean) {
        val session = session.value ?: return
        sessionRepository.updateSession(
            session.copy(
                page = page,
                canLoadMore = canLoadMore,
            )
        )
    }

    fun getIndexFromPostId(postId: Int): Int? {
        val index = posts.value.indexOfFirst { it.id == postId }.takeIf { it != -1 }
        return when {
            index != null -> index + 1
            else -> null
        }
    }

    override fun onCleared() {
        super.onCleared()

        viewModelScope.launch(SupervisorJob()) {
            sessionRepository.delete(sessionId)
        }
    }

    companion object {
        private const val STATE_KEY_IS_LOAD_FROM_SESSION = "IS_LOAD_FROM_SESSION"
    }

    sealed interface Event {
        data class OnJumpToPosition(val session: Session) : Event
    }
}
