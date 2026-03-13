package com.uragiristereo.mikansei.core.product.shared.postfavoritevote

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.PostVote
import com.uragiristereo.mikansei.core.domain.module.database.PostFavoriteVoteRepository
import com.uragiristereo.mikansei.core.domain.module.database.PostRepository
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.domain.module.database.entity.PostFavoriteVote
import com.uragiristereo.mikansei.core.domain.usecase.GetPostWithFavoriteVoteUseCase
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.ui.navigation.SharedRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class PostFavoriteVoteViewModel(
    private val postId: Int,
    private val danbooruRepository: DanbooruRepository,
    private val postRepository: PostRepository,
    private val postFavoriteVoteRepository: PostFavoriteVoteRepository,
    private val userRepository: UserRepository,
    private val getPostWithFavoriteVoteUseCase: GetPostWithFavoriteVoteUseCase,
) : ViewModel() {
    val activeUser; get() = userRepository.active.value

    private val notLoggedInChannel = Channel<Unit>()
    val notLoggedInEvent = notLoggedInChannel.receiveAsFlow()

    val post = postRepository.getPost(postId).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null,
    )

    val favoriteVote = postFavoriteVoteRepository.get(
        postId = postId,
        userId = activeUser.id,
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null,
    )

    val favoriteCount = post.map {
        it?.favorites
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null,
    )

    val voteCount = post.map {
        it?.score
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null,
    )

    private var getFavoriteVoteJob: Job? = null
    private var updateFavoriteVoteJob: Job? = null
    private var updateVoteJob: Job? = null

    init {
        getFavoriteVote(throttle = false)
    }

    private fun getFavoriteVote(throttle: Boolean = true) {
        getFavoriteVoteJob?.cancel()
        getFavoriteVoteJob = viewModelScope.launch(Dispatchers.IO + SupervisorJob()) {
            if (throttle) {
                delay(timeMillis = 500L)
            }

            when (val result = getPostWithFavoriteVoteUseCase.invoke(postId)) {
                is Result.Success -> {
                    Timber.d("post $postId updated")
                    Timber.d("post $postId in favorites = ${result.data.isInFavorites}")
                    Timber.d("post $postId vote status = ${result.data.voteStatus}")

                    if (isActive) {
                        postFavoriteVoteRepository.update(
                            postId = postId,
                            userId = activeUser.id,
                            item = result.data,
                        )
                    }
                }

                is Result.Failed -> Timber.d(result.message)
                is Result.Error -> Timber.d(result.t.toString())
            }
        }
    }

    fun onFavoriteChange(favorite: Boolean) {
        checkUserLogin {
            getFavoriteVoteJob?.cancel()
            updateFavoriteVoteJob?.cancel()
            updateFavoriteVoteJob = viewModelScope.launch(SupervisorJob()) {
                val post = postRepository.getPost(postId).first() ?: return@launch
                Timber.d("post $postId change favorite = $favorite")
                val newFavoriteCount = when {
                    favorite -> post.favorites + 1
                    else -> post.favorites - 1
                }
                val voteStatus = favoriteVote.value?.voteStatus ?: PostVote.Status.NONE
                var newVoteCount = post.score
                var newVoteStatus = voteStatus

                if (favorite && voteStatus == PostVote.Status.NONE) {
                    Timber.d("post $postId change vote status from favorite = ${PostVote.Status.UPVOTED}")
                    newVoteCount += 1
                    newVoteStatus = PostVote.Status.UPVOTED
                } else if (!favorite && voteStatus == PostVote.Status.UPVOTED) {
                    Timber.d("post $postId change vote status from favorite = ${PostVote.Status.NONE}")
                    newVoteStatus = PostVote.Status.NONE
                    newVoteCount -= 1
                }

                postRepository.update(
                    post.copy(
                        favorites = newFavoriteCount,
                        score = newVoteCount,
                    )
                )

                postFavoriteVoteRepository.update(
                    postId = postId,
                    userId = activeUser.id,
                    item = PostFavoriteVote(
                        isInFavorites = favorite,
                        voteStatus = newVoteStatus,
                    ),
                )

                delay(timeMillis = 500L)

                val result = when {
                    favorite -> danbooruRepository.addToFavorites(postId)
                    else -> danbooruRepository.deleteFromFavorites(postId)
                }

                when (result) {
                    is Result.Success -> {
                        Timber.d("post $postId successfully set favorite = $favorite")

                        if (isActive) {
                            getFavoriteVote()
                        }
                    }

                    is Result.Failed -> Timber.d(result.message)
                    is Result.Error -> Timber.d(result.t.toString())
                }
            }
        }
    }

    fun onVoteChange(voteStatus: PostVote.Status) {
        checkUserLogin {
            getFavoriteVoteJob?.cancel()
            updateVoteJob?.cancel()
            updateVoteJob = viewModelScope.launch(SupervisorJob()) {
                val post = postRepository.getPost(postId).first() ?: return@launch
                val favoriteVote = favoriteVote.value ?: return@launch
                Timber.d("post $postId change vote status = $voteStatus")
                val newVoteCount = when (voteStatus) {
                    PostVote.Status.NONE -> {
                        when (favoriteVote.voteStatus) {
                            PostVote.Status.UPVOTED -> post.score - 1
                            PostVote.Status.DOWNVOTED -> post.score + 1
                            PostVote.Status.NONE -> post.score
                        }
                    }

                    PostVote.Status.UPVOTED -> post.score + 1
                    PostVote.Status.DOWNVOTED -> post.score - 1
                }

                postRepository.update(post.copy(score = newVoteCount))
                postFavoriteVoteRepository.update(
                    postId = postId,
                    userId = activeUser.id,
                    item = favoriteVote.copy(voteStatus = voteStatus),
                )

                delay(timeMillis = 500L)

                val result = danbooruRepository.votePost(
                    postId = postId,
                    score = voteStatus,
                )

                when (result) {
                    is Result.Success -> {
                        Timber.d("post $postId successfully set vote status = $voteStatus")

                        if (isActive) {
                            getFavoriteVote()
                        }
                    }

                    is Result.Failed -> Timber.d(result.message)
                    is Result.Error -> Timber.d(result.t.toString())
                }
            }
        }
    }

    private fun checkUserLogin(onLoggedIn: () -> Unit) {
        if (activeUser.isNotAnonymous()) {
            onLoggedIn()
        } else {
            viewModelScope.launch {
                notLoggedInChannel.send(Unit)
            }
        }
    }
}

@Composable
fun SharedRoute.PostId.postFavoriteVoteViewModel(): PostFavoriteVoteViewModel {
    return koinViewModel<PostFavoriteVoteViewModel> {
        parametersOf(postId)
    }
}
