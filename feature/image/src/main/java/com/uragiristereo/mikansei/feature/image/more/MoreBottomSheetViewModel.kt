package com.uragiristereo.mikansei.feature.image.more

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.uragiristereo.safer.compose.navigation.core.getData
import com.uragiristereo.mikansei.core.danbooru.repository.DanbooruRepository
import com.uragiristereo.mikansei.core.database.dao.user.UserDao
import com.uragiristereo.mikansei.core.database.dao.user.toUser
import com.uragiristereo.mikansei.core.domain.entity.post.vote.PostVoteState
import com.uragiristereo.mikansei.core.domain.entity.tag.Tag
import com.uragiristereo.mikansei.core.domain.usecase.ConvertFileSizeUseCase
import com.uragiristereo.mikansei.core.domain.usecase.GetFileSizeUseCase
import com.uragiristereo.mikansei.core.domain.usecase.GetPostUseCase
import com.uragiristereo.mikansei.core.domain.usecase.GetPostVoteUseCase
import com.uragiristereo.mikansei.core.domain.usecase.GetTagsUseCase
import com.uragiristereo.mikansei.core.model.danbooru.post.Post
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.feature.image.more.core.ScoreState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class MoreBottomSheetViewModel(
    savedStateHandle: SavedStateHandle,
    private val danbooruRepository: DanbooruRepository,
    userDao: UserDao,
    private val getTagsUseCase: GetTagsUseCase,
    private val getFileSizeUseCase: GetFileSizeUseCase,
    private val convertFileSizeUseCase: ConvertFileSizeUseCase,
    private val getPostUseCase: GetPostUseCase,
    private val getPostVoteUseCase: GetPostVoteUseCase,
) : ViewModel() {
    private var post = savedStateHandle.getData<MainRoute.Image>()!!.post

    // TODO: avoid using runBlocking here
    private val activeUser = runBlocking {
        userDao.getActive()
            .first()
            .toUser()
    }

    var infoExpanded by mutableStateOf(false)
    var tagsExpanded by mutableStateOf(false)
    var closeButtonHeight by mutableStateOf(0.dp)

    var loading by mutableStateOf(false)
    val tags = mutableStateListOf<Tag>()
    var errorMessage by mutableStateOf<String?>(null)

    var scaledImageFileSizeStr by mutableStateOf("")
    var originalImageFileSizeStr by mutableStateOf("")

    private val customTabsIntentBuilder = CustomTabsIntent.Builder()

    var isPostInFavorites by mutableStateOf(false)
        private set

    var favoriteCount by mutableStateOf(post.favoriteCount)
        private set

    var scoreState by mutableStateOf(ScoreState.NONE)
        private set

    var score by mutableStateOf(post.score)
        private set

    init {
        viewModelScope.launch {
            danbooruRepository.isPostInFavorites(
                postId = post.id,
                userId = activeUser.id,
            ).collect { result ->
                when (result) {
                    is Result.Success -> {
                        isPostInFavorites = result.data

                        Timber.d("is post in favorites = $isPostInFavorites")
                    }

                    is Result.Failed -> Timber.d(result.message)
                    is Result.Error -> Timber.d(result.t.toString())
                }
            }
        }

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
        }

        viewModelScope.launch {
            getPostVoteUseCase(
                postId = post.id,
                userId = activeUser.id,
            ).collect { result ->
                when (result) {
                    is Result.Success -> {
                        val postVote = result.data

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
    }

    fun collapseAll() {
        infoExpanded = false
        tagsExpanded = false
    }

    fun getTags(tagsString: List<String>) {
        tagsExpanded = true

        if (tags.isEmpty()) {
            viewModelScope.launch {
                loading = true

                getTagsUseCase(tags = tagsString)
                    .collect { result ->
                        when (result) {
                            is Result.Success -> {
                                errorMessage = null
                                tags.addAll(result.data)
                                loading = false
                            }

                            is Result.Failed -> {
                                errorMessage = result.message
                                Timber.d(errorMessage)
                                loading = false
                            }

                            is Result.Error -> {
                                errorMessage = result.t.toString()
                                Timber.d(errorMessage)
                                loading = false
                            }
                        }
                    }
            }
        }
    }

    fun getImagesFileSize(post: Post) {
        viewModelScope.launch {
            launch {
                if (post.hasScaled) {
                    getFileSizeUseCase(
                        url = post.scaledImage.url,
                        onLoading = { loading ->
                            if (loading) {
                                scaledImageFileSizeStr = "Loading..."
                            }
                        },
                        onSuccess = { size ->
                            scaledImageFileSizeStr = convertFileSizeUseCase(size)
                        },
                        onFailed = {
                            scaledImageFileSizeStr = "Error!"
                        },
                        onError = {
                            scaledImageFileSizeStr = "Error!"
                        }
                    )
                }
            }

            launch {
                getFileSizeUseCase(
                    url = post.image.url,
                    onLoading = { loading ->
                        if (loading) {
                            originalImageFileSizeStr = "Loading..."
                        }
                    },
                    onSuccess = { size ->
                        originalImageFileSizeStr = convertFileSizeUseCase(size)
                    },
                    onFailed = {
                        originalImageFileSizeStr = "Error!"
                    },
                    onError = {
                        originalImageFileSizeStr = "Error!"
                    }
                )
            }
        }
    }

    fun launchUrl(context: Context, url: String) {
        val parsed = when {
            url.isAHttpUrl() -> url
            else -> "https://www.google.com/search?q=$url"
        }

        customTabsIntentBuilder
            .build()
            .launchUrl(context, Uri.parse(parsed))
    }

    private fun String.isAHttpUrl(): Boolean {
        val http = "http://"
        val https = "https://"

        return when {
            length <= https.length -> false
            substring(startIndex = 0, endIndex = https.length) == https -> true
            substring(startIndex = 0, endIndex = http.length) == http -> true
            else -> false
        }
    }

    fun toggleFavorite(value: Boolean) {
        isPostInFavorites = value

        viewModelScope.launch {
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

    fun upvotePost() {
        scoreState = ScoreState.UPVOTED
        votePost()
    }

    fun downvotePost() {
        scoreState = ScoreState.DOWNVOTED
        votePost()
    }

    private fun votePost() {
        updateScore()

        val scoreToVote = when (scoreState) {
            ScoreState.UPVOTED -> 1
            ScoreState.DOWNVOTED -> -1
            ScoreState.NONE -> 0
        }

        viewModelScope.launch {
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
    }

    fun unvotePost() {
        viewModelScope.launch {
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
}
