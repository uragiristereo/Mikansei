package com.uragiristereo.mikansei.feature.image.image

import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.github.uragiristereo.safer.compose.navigation.core.getData
import com.uragiristereo.mikansei.core.database.dao.user.UserDao
import com.uragiristereo.mikansei.core.domain.module.database.model.toProfile
import com.uragiristereo.mikansei.core.model.preferences.user.DetailSizePreference
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.feature.image.image.core.ImageLoadingState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ImageViewModel(
    savedStateHandle: SavedStateHandle,
    userDao: UserDao,
) : ViewModel() {
    val post = checkNotNull(savedStateHandle.getData<MainRoute.Image>()).post

    val activeUser = runBlocking {
        userDao.getActive().first().toProfile()
    }

    val offsetY = Animatable(0f)
    var loadingState by mutableStateOf(ImageLoadingState.FROM_LOAD)
    var expandButtonVisible by mutableStateOf(post.medias.hasScaled && activeUser.danbooru.defaultImageSize == DetailSizePreference.COMPRESSED)
    var currentZoom by mutableStateOf(1f)

    val isGesturesAllowed by derivedStateOf {
        currentZoom == 1f
    }

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
}
