package com.uragiristereo.mikansei.feature.image.image

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import me.saket.telephoto.zoomable.DoubleClickToZoomListener
import me.saket.telephoto.zoomable.ZoomableImageState
import me.saket.telephoto.zoomable.coil.ZoomableAsyncImage

@Composable
internal fun ImageViewer(
    model: Any?,
    state: ZoomableImageState,
    onDoubleClick: DoubleClickToZoomListener,
    loadingVisible: Boolean,
    onTap: () -> Unit,
    onLongPress: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier.background(Color.Black),
    ) {
        ZoomableAsyncImage(
            model = model,
            contentDescription = null,
            state = state,
            onClick = { onTap() },
            onLongClick = { onLongPress() },
            onDoubleClick = onDoubleClick,
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
