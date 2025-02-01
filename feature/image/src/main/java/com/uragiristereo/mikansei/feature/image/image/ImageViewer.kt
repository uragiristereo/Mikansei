package com.uragiristereo.mikansei.feature.image.image

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBarsIgnoringVisibility
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.viewinterop.AndroidView
import com.ortiz.touchview.TouchImageView

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ImageViewer(
    imageView: TouchImageView,
    loadingVisible: Boolean,
    onTap: () -> Unit,
    onLongPress: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier.background(Color.Black),
    ) {
        Box(
            modifier = Modifier
                .windowInsetsPadding(
                    WindowInsets.systemBarsIgnoringVisibility.only(WindowInsetsSides.Bottom)
                )
                .pointerInput(key1 = Unit) {
                    detectTapGestures(
                        onTap = {
                            onTap()
                        },
                        onLongPress = {
                            onLongPress()
                        },
                    )
                },
        )
        AndroidView(
            factory = { imageView },
            modifier = modifier.fillMaxSize(),
        )

        AnimatedVisibility(
            visible = loadingVisible,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier.fillMaxSize(),
                content = {
                    CircularProgressIndicator()
                },
            )
        }
    }
}
