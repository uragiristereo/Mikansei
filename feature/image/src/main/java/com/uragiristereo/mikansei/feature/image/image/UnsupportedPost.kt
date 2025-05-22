package com.uragiristereo.mikansei.feature.image.image

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ui.Scaffold
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.LocalLambdaOnDownload
import com.uragiristereo.mikansei.core.ui.LocalScaffoldState
import com.uragiristereo.mikansei.core.ui.LocalWindowSizeHorizontal
import com.uragiristereo.mikansei.core.ui.WindowSize
import com.uragiristereo.mikansei.feature.image.core.verticallyDraggable
import com.uragiristereo.mikansei.feature.image.image.appbars.ImageBottomAppBar
import com.uragiristereo.mikansei.feature.image.image.appbars.ImageTopAppBar
import org.koin.androidx.compose.koinViewModel
import kotlin.math.abs

@Composable
internal fun UnsupportedPost(
    onNavigateBack: (Boolean) -> Unit,
    onMoreClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ImageViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val lambdaOnDownload = LocalLambdaOnDownload.current
    val windowSizeHorizontal = LocalWindowSizeHorizontal.current

    Scaffold(
        scaffoldState = LocalScaffoldState.current,
        topBar = {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .graphicsLayer {
                        translationY = -abs(viewModel.offsetY.value)
                    },
            ) {
                ImageTopAppBar(
                    post = viewModel.post,
                    expandLoadingVisible = false,
                    expandButtonVisible = false,
                    onNavigateBack = {
                        onNavigateBack(/* navigatedBackByGesture = */ false)
                    },
                    onExpandClick = {},
                    onDownloadClick = lambdaOnDownload,
                    onShareClick = onShareClick,
                    onMoreClick = onMoreClick,
                )
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = windowSizeHorizontal == WindowSize.COMPACT,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .graphicsLayer {
                        translationY = abs(viewModel.offsetY.value)
                    },
            ) {
                ImageBottomAppBar(
                    post = viewModel.post,
                    expandLoadingVisible = false,
                    expandButtonVisible = false,
                    onExpandClick = {},
                    onDownloadClick = lambdaOnDownload,
                    onShareClick = onShareClick,
                )
            }
        },
        contentPadding = PaddingValues(0.dp),
        modifier = modifier,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .graphicsLayer {
                    translationY = viewModel.offsetY.value
                }
                .pointerInput(key1 = Unit) {
                    detectTapGestures(
                        onLongPress = {
                            onMoreClick()
                        },
                    )
                }
                .verticallyDraggable(
                    enabled = true,
                    offsetY = viewModel.offsetY,
                    onDragExit = {
                        onNavigateBack(true)
                    },
                )
                .padding(all = 16.dp),
        ) {
            Icon(
                painter = painterResource(R.drawable.image_not_supported),
                contentDescription = null,
                tint = MaterialTheme.colors.onPrimary,
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.primary)
                    .padding(16.dp)
                    .size(48.dp),
            )

            Text(
                text = "This post is not supported for viewing in Mikansei",
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp),
            )

            Button(
                shape = RoundedCornerShape(percent = 50),
                onClick = {
                    viewModel.openPostInBrowser(context)
                },
            ) {
                Icon(
                    painter = painterResource(R.drawable.open_in_browser),
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp),
                )

                Text(text = "Open in Browser")
            }
        }
    }
}
