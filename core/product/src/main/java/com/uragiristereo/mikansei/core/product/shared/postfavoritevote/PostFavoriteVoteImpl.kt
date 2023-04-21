package com.uragiristereo.mikansei.core.product.shared.postfavoritevote

import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.uragiristereo.safer.compose.navigation.core.getData
import com.uragiristereo.mikansei.core.danbooru.repository.DanbooruRepository
import com.uragiristereo.mikansei.core.database.dao.user.UserDao
import com.uragiristereo.mikansei.core.database.dao.user.toUser
import com.uragiristereo.mikansei.core.domain.entity.post.vote.PostVoteState
import com.uragiristereo.mikansei.core.domain.usecase.GetPostUseCase
import com.uragiristereo.mikansei.core.domain.usecase.GetPostVoteUseCase
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.model.user.isAnonymous
import com.uragiristereo.mikansei.core.model.user.isNotAnonymous
import com.uragiristereo.mikansei.core.product.shared.postfavoritevote.core.ScoreState
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

open class PostFavoriteVoteImpl : ViewModel(), PostFavoriteVote, KoinComponent {
    private val savedStateHandle: SavedStateHandle by inject()
    private val danbooruRepository: DanbooruRepository by inject()
    private val userDao: UserDao by inject()
    private val getPostUseCase: GetPostUseCase by inject()
    private val getPostVoteUseCase: GetPostVoteUseCase by inject()

    // TODO: avoid using runBlocking here
    private val activeUser = runBlocking {
        userDao.getActive().first().toUser()
    }

    override var post = savedStateHandle.getData<MainRoute.Image>()!!.post

    private val post2 by lazy { post }

    override var isPostInFavorites by mutableStateOf(false)
    override var favoriteCount by mutableStateOf(post2.favoriteCount)
    override var scoreState by mutableStateOf(ScoreState.NONE)
    override var score by mutableStateOf(post2.score)
    override var isPostUpdated by mutableStateOf(false)
    override var favoriteButtonEnabled by mutableStateOf(false)
    override var voteButtonEnabled by mutableStateOf(false)

    override val toastChannel = Channel<Pair<String, Int>>()

    init {
        viewModelScope.launch {
            getPostUseCase(id = post.id).collect { result ->
                when (result) {
                    is Result.Success -> {
                        post = result.data
                        favoriteCount = post.favoriteCount
                        score = post.score

                        Timber.d("post updated")
                    }

                    is Result.Failed -> Timber.d(result.message)
                    is Result.Error -> Timber.d(result.t.toString())
                }
            }

            isPostUpdated = true
        }

        viewModelScope.launch {
            if (activeUser.isNotAnonymous()) {
                danbooruRepository.isPostInFavorites(
                    postId = post.id,
                    userId = activeUser.id,
                ).collect { result ->
                    when (result) {
                        is Result.Success -> {
                            favoriteButtonEnabled = true
                            isPostInFavorites = result.data

                            Timber.d("is post in favorites = $isPostInFavorites")
                        }

                        is Result.Failed -> Timber.d(result.message)
                        is Result.Error -> Timber.d(result.t.toString())
                    }
                }
            }

            favoriteButtonEnabled = true
        }

        viewModelScope.launch {
            if (activeUser.isNotAnonymous()) {
                getPostVoteUseCase(
                    postId = post.id,
                    userId = activeUser.id,
                ).collect { result ->
                    when (result) {
                        is Result.Success -> {
                            val postVote = result.data
                            voteButtonEnabled = true

                            if (postVote != null) {
                                scoreState = when (postVote.state) {
                                    PostVoteState.UPVOTED -> ScoreState.UPVOTED
                                    else -> ScoreState.DOWNVOTED
                                }
                            }

                            Timber.d("score state = $scoreState")
                        }

                        is Result.Failed -> Timber.d(result.message)
                        is Result.Error -> Timber.d(result.t.toString())
                    }
                }
            }

            voteButtonEnabled = true
        }
    }

    override fun toggleFavorite(value: Boolean) {
        viewModelScope.launch {
            if (activeUser.isAnonymous()) {
                toastChannel.send("Please Login to use this feature!" to Toast.LENGTH_LONG)

                return@launch
            }

            isPostInFavorites = value

            when {
                isPostInFavorites -> {
                    favoriteCount += 1

                    if (scoreState == ScoreState.NONE) {
                        scoreState = ScoreState.UPVOTED
                        updateScore()
                    }

                    danbooruRepository.addToFavorites(post.id).collect { result ->
                        when (result) {
                            is Result.Success -> Timber.d("post ${post.id} successfully added to favorites")
                            is Result.Failed -> Timber.d(result.message)
                            is Result.Error -> Timber.d(result.t.toString())
                        }
                    }
                }

                else -> {
                    favoriteCount -= 1

                    if (scoreState == ScoreState.UPVOTED) {
                        scoreState = ScoreState.NONE
                        score -= 1
                    }

                    danbooruRepository.deleteFromFavorites(post.id).collect { result ->
                        when (result) {
                            is Result.Success -> Timber.d("post ${post.id} successfully removed from favorites")
                            is Result.Failed -> Timber.d(result.message)
                            is Result.Error -> Timber.d(result.t.toString())
                        }
                    }
                }
            }
        }
    }

    private fun updateScore() {
        score = when (scoreState) {
            ScoreState.UPVOTED -> post.score + 1
            ScoreState.DOWNVOTED -> post.score - 1
            ScoreState.NONE -> post.score
        }
    }

    override fun upvotePost() {
        viewModelScope.launch {
            if (activeUser.isAnonymous()) {
                toastChannel.send("Please Login to use this feature!" to Toast.LENGTH_LONG)

                return@launch
            }

            scoreState = ScoreState.UPVOTED
            votePost()
        }
    }

    override fun downvotePost() {
        viewModelScope.launch {
            if (activeUser.isAnonymous()) {
                toastChannel.send("Please Login to use this feature!" to Toast.LENGTH_LONG)

                return@launch
            }

            scoreState = ScoreState.DOWNVOTED
            votePost()
        }
    }

    private suspend fun votePost() {
        updateScore()

        val scoreToVote = when (scoreState) {
            ScoreState.UPVOTED -> 1
            ScoreState.DOWNVOTED -> -1
            ScoreState.NONE -> 0
        }

        danbooruRepository.votePost(
            postId = post.id,
            score = scoreToVote,
        ).collect { result ->
            when (result) {
                is Result.Success -> Timber.d("post ${post.id} successfully ${scoreState.name}")
                is Result.Failed -> Timber.d(result.message)
                is Result.Error -> Timber.d(result.t.toString())
            }
        }
    }

    override fun unvotePost() {
        viewModelScope.launch {
            if (activeUser.isAnonymous()) {
                toastChannel.send("Please Login to use this feature!" to Toast.LENGTH_LONG)

                return@launch
            }

            score = when (scoreState) {
                ScoreState.UPVOTED -> score - 1
                ScoreState.DOWNVOTED -> score + 1
                else -> score
            }

            scoreState = ScoreState.NONE

            danbooruRepository.unvotePost(postId = post.id).collect { result ->
                when (result) {
                    is Result.Success -> Timber.d("post ${post.id} successfully unvoted")
                    is Result.Failed -> Timber.d(result.message)
                    is Result.Error -> Timber.d(result.t.toString())
                }
            }
        }
    }

    override fun onVoteChange(value: ScoreState) {
        when (value) {
            ScoreState.UPVOTED -> upvotePost()
            ScoreState.DOWNVOTED -> downvotePost()
            ScoreState.NONE -> unvotePost()
        }
    }
}
