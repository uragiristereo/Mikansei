package com.uragiristereo.mikansei.feature.image.more

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.widget.Toast
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
import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Tag
import com.uragiristereo.mikansei.core.domain.usecase.ConvertFileSizeUseCase
import com.uragiristereo.mikansei.core.domain.usecase.GetFileSizeUseCase
import com.uragiristereo.mikansei.core.domain.usecase.GetTagsUseCase
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.product.shared.postfavoritevote.PostFavoriteVote
import com.uragiristereo.mikansei.core.product.shared.postfavoritevote.PostFavoriteVoteImpl
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import kotlinx.coroutines.launch
import timber.log.Timber

class MoreBottomSheetViewModel(
    savedStateHandle: SavedStateHandle,
    private val danbooruRepository: DanbooruRepository,
    private val getTagsUseCase: GetTagsUseCase,
    private val getFileSizeUseCase: GetFileSizeUseCase,
    private val convertFileSizeUseCase: ConvertFileSizeUseCase,
) : ViewModel(),
    PostFavoriteVote by PostFavoriteVoteImpl() {
    override var post = savedStateHandle.getData<MainRoute.Image>()!!.post

    var infoExpanded by mutableStateOf(false)
    var tagsExpanded by mutableStateOf(false)
    var closeButtonHeight by mutableStateOf(0.dp)

    var loading by mutableStateOf(false)
    val tags = mutableStateListOf<Tag>()
    var errorMessage by mutableStateOf<String?>(null)

    var scaledImageFileSizeStr by mutableStateOf("")
    var originalImageFileSizeStr by mutableStateOf("")

    private val customTabsIntentBuilder = CustomTabsIntent.Builder()

    var uploaderName by mutableStateOf(post.uploaderId.toString())
        private set

    init {
        getUploaderName()
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
                post.medias.scaled?.let { media ->
                    getFileSizeUseCase(
                        url = media.url,
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
                    url = post.medias.original.url,
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

    private fun getUploaderName() {
        viewModelScope.launch {
            Timber.d("getting uploader name...")

            danbooruRepository.getUser(id = post.uploaderId).collect { result ->
                when (result) {
                    is Result.Success -> {
                        val user = result.data
                        uploaderName = user.name

                        Timber.d("uploader name = $uploaderName")
                    }

                    is Result.Failed -> Timber.d("Error: ${result.message}")
                    is Result.Error -> Timber.d("Error: ${result.t}")
                }
            }
        }
    }

    fun copyToClipboard(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(text, text)
        clipboard.setPrimaryClip(clip)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            Toast
                .makeText(context, "Source copied to clipboard!", Toast.LENGTH_SHORT)
                .show()
        }
    }
}
