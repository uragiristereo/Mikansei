package com.uragiristereo.mikansei.feature.image

import androidx.annotation.OptIn
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.navigation.toRoute
import com.uragiristereo.mikansei.core.domain.module.database.SessionRepository
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.domain.module.database.entity.Session
import com.uragiristereo.mikansei.core.domain.module.network.NetworkRepository
import com.uragiristereo.mikansei.core.domain.usecase.GetPostsUseCase
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.core.ui.navigation.PostNavType
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

class ViewerViewModel(
    private val savedStateHandle: SavedStateHandle,
    userRepository: UserRepository,
    private val sessionRepository: SessionRepository,
    private val networkRepository: NetworkRepository,
    private val getPostsUseCase: GetPostsUseCase,
) : ViewModel() {
    private val args = savedStateHandle.toRoute<MainRoute.Image>(PostNavType)
    private val sessionId = args.sessionId

    val targetPostId = savedStateHandle.getStateFlow(
        key = "targetPostId",
        initialValue = args.targetPostId,
    )

    val session = sessionRepository.getSession(sessionId)
        .filterNotNull()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = Session(
                id = sessionId,
                tags = "",
            ),
        )

    val posts = sessionRepository
        .getPosts(sessionId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000L),
            initialValue = emptyList(),
        )

    var currentZoom by mutableFloatStateOf(1f)
    var pinchGesture by mutableStateOf(false)
    val offsetY = mutableFloatStateOf(0f)

    val gesturesEnabled by derivedStateOf {
        currentZoom == 1f && !pinchGesture
    }

    val pagerSwipeEnabled by derivedStateOf {
        gesturesEnabled && offsetY.floatValue == 0f
    }

    private var postsLoading by mutableStateOf(false)

    val activeUser = userRepository.active

    var areAppBarsVisible by mutableStateOf(true)
        private set

    fun setAppBarsVisible(value: Boolean) {
        areAppBarsVisible = value
    }

    fun toggleAppBarsVisible() {
        areAppBarsVisible = !areAppBarsVisible
    }

    fun setTargetPostId(postId: Int) {
        savedStateHandle["targetPostId"] = postId
    }

    fun getPosts() {
        viewModelScope.launch {
            if (!postsLoading) {
                Timber.d("getting new posts")
                postsLoading = true
                val newPage = session.value.page + 1

                val result = getPostsUseCase.invoke(
                    sessionId = sessionId,
                    tags = session.value.tags,
                    page = newPage,
                )

                when (result) {
                    is Result.Success -> {
                        Timber.d("getting new posts success")

                        updateSessionResult(
                            page = newPage,
                            canLoadMore = result.data.canLoadMore,
                        )
                    }

                    is Result.Error -> {
                        Timber.d(result.t.toString())
                    }

                    is Result.Failed -> {
                        Timber.d(result.message)
                    }
                }

                postsLoading = false
            }
        }
    }

    @OptIn(UnstableApi::class)
    fun getMediaSourceFactory(): MediaSource.Factory {
        return ProgressiveMediaSource.Factory(networkRepository.exoPlayerCacheFactory)
    }

    private suspend fun updateSessionResult(page: Int, canLoadMore: Boolean) {
        sessionRepository.updateSession(
            session.value.copy(
                page = page,
                canLoadMore = canLoadMore,
            )
        )
    }
}
