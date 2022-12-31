package com.uragiristereo.mejiboard.presentation.image.more

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uragiristereo.mejiboard.common.helper.NumberHelper
import com.uragiristereo.mejiboard.domain.entity.source.post.Post
import com.uragiristereo.mejiboard.domain.entity.source.tag.Tag
import com.uragiristereo.mejiboard.domain.usecase.GetFileSizeUseCase
import com.uragiristereo.mejiboard.domain.usecase.GetTagsUseCase
import kotlinx.coroutines.launch

class MoreBottomSheetViewModel(
    private val getTagsUseCase: GetTagsUseCase,
    private val getFileSizeUseCase: GetFileSizeUseCase,
) : ViewModel() {
    var infoExpanded by mutableStateOf(false)
    var tagsExpanded by mutableStateOf(false)
    var closeButtonHeight by mutableStateOf(0.dp)

    var loading by mutableStateOf(false)
    val tags = mutableStateListOf<Tag>()
    var errorMessage by mutableStateOf<String?>(null)

    var scaledImageFileSizeStr by mutableStateOf("")
    var originalImageFileSizeStr by mutableStateOf("")

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

    fun getImagesFileSize(post: Post) {
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
                            scaledImageFileSizeStr = NumberHelper.convertFileSize(size)
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
                        originalImageFileSizeStr = NumberHelper.convertFileSize(size)
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
}
