package com.uragiristereo.mikansei.feature.image.image

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Profile
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.model.preferences.user.DetailSizePreference
import com.uragiristereo.mikansei.feature.image.image.core.ImageLoadingState
import kotlinx.coroutines.flow.StateFlow

class ImageViewModel(
    private val userRepository: UserRepository,
    danbooruRepository: DanbooruRepository,
    val post: Post,
) : ViewModel() {

    val activeUser: StateFlow<Profile>
        get() = userRepository.active

    var loadingState by mutableStateOf(ImageLoadingState.FROM_LOAD)
    var expandButtonVisible by mutableStateOf(post.medias.hasScaled && activeUser.value.danbooru.defaultImageSize == DetailSizePreference.COMPRESSED)

    private val customTabsIntentBuilder = CustomTabsIntent.Builder()
    private val postLink = danbooruRepository.host.parsePostLink(post.id)

    fun onExpandImage() {
        expandButtonVisible = false
        loadingState = ImageLoadingState.FROM_EXPAND
    }

    fun resizeImage(width: Int, height: Int): Pair<Int, Int> {
        val maxSize = 4096f

        return when {
            width > maxSize || height > maxSize -> {
                val scale = when {
                    width > height -> maxSize.div(width)
                    else -> maxSize.div(height)
                }

                val scaledWidth = width * scale
                val scaledHeight = height * scale

                Pair(scaledWidth.toInt(), scaledHeight.toInt())
            }

            else -> Pair(width, height)
        }
    }

    fun openPostInBrowser(context: Context) {
        customTabsIntentBuilder
            .build()
            .launchUrl(context, postLink.toUri())
    }
}
