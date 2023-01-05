package com.uragiristereo.mejiboard.feature.image

import android.app.Activity
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.uragiristereo.mejiboard.core.common.data.Constants
import com.uragiristereo.mejiboard.core.common.ui.LocalLambdaOnDownload
import com.uragiristereo.mejiboard.core.common.ui.composable.SetSystemBarsColors
import com.uragiristereo.mejiboard.core.common.ui.extension.hideSystemBars
import com.uragiristereo.mejiboard.core.common.ui.extension.showSystemBars
import com.uragiristereo.mejiboard.feature.image.appbars.ImageBottomAppBar
import com.uragiristereo.mejiboard.feature.image.appbars.ImageTopAppBar
import com.uragiristereo.mejiboard.feature.image.image.ImagePost
import com.uragiristereo.mejiboard.feature.image.more.MoreBottomSheet
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.math.abs

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ImageScreen(
    onNavigateBack: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ImageViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val window = (context as Activity).window
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val hapticFeedback = LocalHapticFeedback.current
    val lambdaOnDownload = LocalLambdaOnDownload.current

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    val maxOffset = remember { with(density) { 100.dp.toPx() } }
    val showExpandButton = !viewModel.showOriginalImage && !viewModel.originalImageShown && viewModel.post.scaled

    val lambdaOnMoreClick: () -> Unit = {
        scope.launch {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            sheetState.animateTo(ModalBottomSheetValue.Expanded)
        }
    }

    val lambdaOnExpandClick: () -> Unit = remember {
        {
            viewModel.originalImageShown = true
        }
    }

    LaunchedEffect(key1 = viewModel.appBarsVisible) {
        when {
            viewModel.appBarsVisible -> window.showSystemBars()
            else -> window.hideSystemBars()
        }
    }

    LaunchedEffect(key1 = sheetState.currentValue) {
        if (sheetState.currentValue == ModalBottomSheetValue.Expanded) {
            viewModel.appBarsVisible = true
            window.showSystemBars()
        }
    }

    BackHandler(
        enabled = sheetState.isVisible,
        onBack = {
            scope.launch {
                sheetState.hide()
            }
        },
    )

    SetSystemBarsColors(
        statusBarColor = Color.Black.copy(alpha = 0.4f),
        navigationBarColor = when {
            sheetState.targetValue != ModalBottomSheetValue.Hidden -> Color.Transparent
            else -> Color.Black.copy(alpha = 0.4f)
        },
        statusBarDarkIcons = false,
        navigationBarDarkIcons = when {
            sheetState.targetValue != ModalBottomSheetValue.Hidden -> MaterialTheme.colors.isLight
            else -> false
        },
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        if (viewModel.post.originalImage.fileType in Constants.SUPPORTED_TYPES_IMAGE) {
            ImagePost(
                post = viewModel.post,
                maxOffset = maxOffset,
                onNavigateBack = onNavigateBack,
                onMoreClick = lambdaOnMoreClick,
            )
        }

        ImageTopAppBar(
            postId = viewModel.post.id,
            visible = viewModel.appBarsVisible,
            originalImageShown = viewModel.originalImageShown,
            loading = viewModel.loading,
            onNavigateBack = {
                onNavigateBack(false)
            },
            onExpandClick = lambdaOnExpandClick,
            onDownloadClick = remember { { lambdaOnDownload(viewModel.post) } },
            onShareClick = { /*TODO*/ },
            onMoreClick = lambdaOnMoreClick,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .let {
                    when (sheetState.currentValue) {
                        ModalBottomSheetValue.Hidden -> it
                            .graphicsLayer {
                                translationY = -abs(viewModel.offsetY.value)
                            }

                        else -> it
                    }
                },
        )

        if (configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) {
            ImageBottomAppBar(
                visible = viewModel.appBarsVisible,
                loading = viewModel.loading,
                showExpandButton = showExpandButton,
                onExpandClick = lambdaOnExpandClick,
                onDownloadClick = remember { { lambdaOnDownload(viewModel.post) } },
                onShareClick = { /*TODO*/ },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .let {
                        when (sheetState.currentValue) {
                            ModalBottomSheetValue.Hidden -> it
                                .graphicsLayer {
                                    translationY = abs(viewModel.offsetY.value)
                                }

                            else -> it
                        }
                    },
            )
        }

        MoreBottomSheet(
            post = viewModel.post,
            sheetState = sheetState,
            showExpandButton = showExpandButton,
            onExpandClick = lambdaOnExpandClick,
            modifier = Modifier
                .alpha(
                    when {
                        viewModel.appBarsVisible -> 1f
                        else -> 0f
                    }
                ),
        )
    }
}
