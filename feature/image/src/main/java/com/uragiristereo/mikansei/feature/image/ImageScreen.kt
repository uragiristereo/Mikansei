package com.uragiristereo.mikansei.feature.image

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsIgnoringVisibility
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import com.uragiristereo.mikansei.core.model.Constants
import com.uragiristereo.mikansei.core.model.ShareOption
import com.uragiristereo.mikansei.core.model.danbooru.post.Post
import com.uragiristereo.mikansei.core.ui.LocalLambdaOnDownload
import com.uragiristereo.mikansei.core.ui.LocalLambdaOnShare
import com.uragiristereo.mikansei.core.ui.composable.SetSystemBarsColors
import com.uragiristereo.mikansei.core.ui.extension.areNavigationBarsButtons
import com.uragiristereo.mikansei.core.ui.extension.hideSystemBars
import com.uragiristereo.mikansei.core.ui.extension.showSystemBars
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.rememberModalBottomSheetState2
import com.uragiristereo.mikansei.feature.image.image.ImagePost
import com.uragiristereo.mikansei.feature.image.more.MoreBottomSheet
import com.uragiristereo.mikansei.feature.image.video.VideoPost
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class, ExperimentalLayoutApi::class)
@Composable
internal fun ImageScreen(
    onNavigateBack: (Boolean) -> Unit,
    onNavigateToAddToFavGroup: (Post) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ViewerViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val window = (context as Activity).window
    val hapticFeedback = LocalHapticFeedback.current
    val lambdaOnDownload = LocalLambdaOnDownload.current
    val lambdaOnShare = LocalLambdaOnShare.current

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState2(initialValue = ModalBottomSheetValue.Hidden)

    val lambdaOnMoreClick: () -> Unit = {
        scope.launch {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            sheetState.animateTo(ModalBottomSheetValue.Expanded)
        }
    }

    LaunchedEffect(key1 = viewModel.areAppBarsVisible) {
        when {
            viewModel.areAppBarsVisible -> window.showSystemBars()
            else -> window.hideSystemBars()
        }
    }

    LaunchedEffect(key1 = sheetState.currentValue) {
        if (sheetState.currentValue == ModalBottomSheetValue.Expanded) {
            viewModel.setAppBarsVisible(true)
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
        statusBarColor = Color.Transparent,
        statusBarDarkIcons = false,
        navigationBarColor = when {
            WindowInsets.navigationBarsIgnoringVisibility.areNavigationBarsButtons() -> Color.Black.copy(
                alpha = 0.7f
            )

            else -> Color.Transparent
        },
        navigationBarDarkIcons = false,
    )

    Box(modifier = modifier) {
        when (val postType = viewModel.post.image.fileType) {
            in Constants.SUPPORTED_TYPES_IMAGE -> {
                ImagePost(
                    onNavigateBack = onNavigateBack,
                    onMoreClick = lambdaOnMoreClick,
                    areAppBarsVisible = viewModel.areAppBarsVisible,
                    onAppBarsVisibleChange = viewModel::setAppBarsVisible,
                )
            }

            in Constants.SUPPORTED_TYPES_VIDEO -> {
                VideoPost(
                    areAppBarsVisible = viewModel.areAppBarsVisible,
                    onAppBarsVisibleChange = viewModel::setAppBarsVisible,
                    onNavigateBack = onNavigateBack,
                    onMoreClick = lambdaOnMoreClick,
                    onDownloadClick = {
                        lambdaOnDownload(viewModel.post)
                    },
                    onShareClick = {
                        lambdaOnShare(
                            viewModel.post,
                            when (postType) {
                                "zip" -> ShareOption.COMPRESSED
                                else -> ShareOption.ORIGINAL
                            },
                        )
                    }
                )
            }
        }

        MoreBottomSheet(
            post = viewModel.post,
            sheetState = sheetState,
            showExpandButton = false,
            onExpandClick = { },
            onAddToClick = {
                scope.launch {
                    sheetState.hide()

                    onNavigateToAddToFavGroup(viewModel.post)
                }
            },
        )
    }
}
