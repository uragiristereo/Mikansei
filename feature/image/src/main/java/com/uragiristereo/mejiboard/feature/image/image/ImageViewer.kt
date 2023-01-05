package com.uragiristereo.mejiboard.feature.image.image

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import com.ortiz.touchview.TouchImageView
import com.uragiristereo.mejiboard.feature.image.core.ImageLoading

@Composable
fun ImageViewer(
    imageView: TouchImageView,
    loading: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        AndroidView(
            factory = { imageView },
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
        )

        ImageLoading(visible = loading)
    }
}
