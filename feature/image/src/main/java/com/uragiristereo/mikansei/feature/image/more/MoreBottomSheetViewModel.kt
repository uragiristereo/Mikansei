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
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Tag
import com.uragiristereo.mikansei.core.domain.module.database.TagCategoryRepository
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.domain.module.network.NetworkRepository
import com.uragiristereo.mikansei.core.domain.usecase.ConvertFileSizeUseCase
import com.uragiristereo.mikansei.core.domain.usecase.GetTagsUseCase
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.product.shared.postfavoritevote.PostFavoriteVote
import com.uragiristereo.mikansei.core.product.shared.postfavoritevote.PostFavoriteVoteImpl
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.core.ui.navigation.PostNavType
import kotlinx.coroutines.launch
import timber.log.Timber

class MoreBottomSheetViewModel(
    savedStateHandle: SavedStateHandle,
    private val danbooruRepository: DanbooruRepository,
    private val networkRepository: NetworkRepository,
    private val userRepository: UserRepository,
    private val tagCategoryRepository: TagCategoryRepository,
    private val getTagsUseCase: GetTagsUseCase,
    private val convertFileSizeUseCase: ConvertFileSizeUseCase,
) : ViewModel(),
    PostFavoriteVote by PostFavoriteVoteImpl() {
    override var post = savedStateHandle.toRoute<MainRoute.More>(PostNavType).post

    var infoExpanded by mutableStateOf(false)
    var tagsExpanded by mutableStateOf(false)

    var loading by mutableStateOf(false)
    val tags = mutableStateListOf<Tag>()
    var errorMessage by mutableStateOf<String?>(null)

    var scaledImageFileSizeStr by mutableStateOf("")
    var originalImageFileSizeStr by mutableStateOf("")

    private val customTabsIntentBuilder = CustomTabsIntent.Builder()

    var uploaderName by mutableStateOf(post.uploaderId.toString())
        private set

    val postLink = danbooruRepository.host.parsePostLink(post.id)
    val activeUser = userRepository.active

    init {
        getUploaderName()
    }

    fun collapseAll() {
        infoExpanded = false
        tagsExpanded = false
    }

    fun getTags() {
        tagsExpanded = true

        if (tags.isEmpty()) {
            viewModelScope.launch {
                loading = true

                when (val result = getTagsUseCase(tags = post.tags)) {
                    is Result.Success -> {
                        errorMessage = null
                        tags.addAll(result.data)

                        val tagsMap = tags.associate { it.name to it.category }
                        tagCategoryRepository.update(tagsMap)
                    }

                    is Result.Failed -> {
                        errorMessage = result.message
                        Timber.d(errorMessage)
                    }

                    is Result.Error -> {
                        errorMessage = result.t.toString()
                        Timber.d(errorMessage)
                    }
                }

                loading = false
            }
        }
    }

    fun getImagesFileSize() {
        viewModelScope.launch {
            launch {
                post.medias.scaled?.let { media ->
                    scaledImageFileSizeStr = "Loading..."

                    networkRepository.getFileSize(media.url).collect { result ->
                        scaledImageFileSizeStr = when (result) {
                            is Result.Success -> convertFileSizeUseCase(result.data)
                            else -> "Error!"
                        }
                    }
                }
            }

            launch {
                originalImageFileSizeStr = "Loading..."

                networkRepository.getFileSize(post.medias.original.url).collect { result ->
                    originalImageFileSizeStr = when (result) {
                        is Result.Success -> convertFileSizeUseCase(result.data)
                        else -> "Error!"
                    }
                }
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

            when (val result = danbooruRepository.getUser(id = post.uploaderId)) {
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
