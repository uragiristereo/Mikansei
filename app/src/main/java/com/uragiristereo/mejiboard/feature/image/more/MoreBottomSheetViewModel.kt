package com.uragiristereo.mejiboard.feature.image.more

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uragiristereo.mejiboard.core.common.data.util.NumberUtil
import kotlinx.coroutines.launch

class MoreBottomSheetViewModel(
    private val getTagsUseCase: com.uragiristereo.mejiboard.domain.usecase.GetTagsUseCase,
    private val getFileSizeUseCase: com.uragiristereo.mejiboard.domain.usecase.GetFileSizeUseCase,
) : ViewModel() {
    var infoExpanded by mutableStateOf(false)
    var tagsExpanded by mutableStateOf(false)
    var closeButtonHeight by mutableStateOf(0.dp)

    var loading by mutableStateOf(false)
    val tags = mutableStateListOf<com.uragiristereo.mejiboard.core.model.booru.tag.Tag>()
    var errorMessage by mutableStateOf<String?>(null)

    var scaledImageFileSizeStr by mutableStateOf("")
    var originalImageFileSizeStr by mutableStateOf("")

    private val customTabsIntentBuilder = CustomTabsIntent.Builder()

    fun collapseAll() {
        infoExpanded = false
        tagsExpanded = false
    }

    fun getTags(tagsString: List<String>) {
        tagsExpanded = true

        if (tags.isEmpty()) {
            viewModelScope.launch {
                getTagsUseCase(
                    tags = tagsString,
                    onLoading = { loading = it },
                    onSuccess = { data ->
                        errorMessage = null
                        tags.addAll(data)
                    },
                    onFailed = { message ->
                        errorMessage = message
                    },
                    onError = { t ->
                        errorMessage = t.toString()
                    },
                )
            }
        }
    }

    fun getImagesFileSize(post: com.uragiristereo.mejiboard.core.model.booru.post.Post) {
        viewModelScope.launch {
            launch {
                if (post.scaled) {
                    getFileSizeUseCase(
                        url = post.scaledImage.url,
                        onLoading = { loading ->
                            if (loading) {
                                scaledImageFileSizeStr = "Loading..."
                            }
                        },
                        onSuccess = { size ->
                            scaledImageFileSizeStr = NumberUtil.convertFileSize(size)
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
                    url = post.originalImage.url,
                    onLoading = { loading ->
                        if (loading) {
                            originalImageFileSizeStr = "Loading..."
                        }
                    },
                    onSuccess = { size ->
                        originalImageFileSizeStr = NumberUtil.convertFileSize(size)
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
}
