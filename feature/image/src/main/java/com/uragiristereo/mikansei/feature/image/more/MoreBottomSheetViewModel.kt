package com.uragiristereo.mikansei.feature.image.more

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
import com.uragiristereo.mikansei.core.domain.entity.tag.Tag
import com.uragiristereo.mikansei.core.domain.usecase.ConvertFileSizeUseCase
import com.uragiristereo.mikansei.core.domain.usecase.GetFileSizeUseCase
import com.uragiristereo.mikansei.core.domain.usecase.GetTagsUseCase
import com.uragiristereo.mikansei.core.model.danbooru.post.Post
import com.uragiristereo.mikansei.core.model.result.Result
import kotlinx.coroutines.launch
import timber.log.Timber

class MoreBottomSheetViewModel(
    private val getTagsUseCase: GetTagsUseCase,
    private val getFileSizeUseCase: GetFileSizeUseCase,
    private val convertFileSizeUseCase: ConvertFileSizeUseCase,
) : ViewModel() {
    var infoExpanded by mutableStateOf(false)
    var tagsExpanded by mutableStateOf(false)
    var closeButtonHeight by mutableStateOf(0.dp)

    var loading by mutableStateOf(false)
    val tags = mutableStateListOf<Tag>()
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
//                getTagsUseCase(
//                    tags = tagsString,
//                    onLoading = { loading = it },
//                    onSuccess = { data ->
//                        errorMessage = null
//                        tags.addAll(data)
//                    },
//                    onFailed = { message ->
//                        errorMessage = message
//                        Timber.d(errorMessage)
//                    },
//                    onError = { t ->
//                        errorMessage = t.toString()
//                        Timber.d(errorMessage)
//                    },
//                )
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
}