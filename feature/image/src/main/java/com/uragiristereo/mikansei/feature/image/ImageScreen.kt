package com.uragiristereo.mikansei.feature.image

import android.app.Activity
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsIgnoringVisibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.ui.LocalLambdaOnDownload
import com.uragiristereo.mikansei.core.ui.composable.SetSystemBarsColors
import com.uragiristereo.mikansei.core.ui.extension.areNavigationBarsButtons
import com.uragiristereo.mikansei.core.ui.extension.hideSystemBars
import com.uragiristereo.mikansei.core.ui.extension.showSystemBars
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator.LocalBottomSheetNavigator
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.feature.image.image.ImagePost
import com.uragiristereo.mikansei.feature.image.image.UnsupportedPost
import com.uragiristereo.mikansei.feature.image.video.VideoPost
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ImageScreen(
    onNavigateBack: (Boolean) -> Unit,
    onNavigateToMore: (Post) -> Unit,
    viewModel: ViewerViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val window = (context as Activity).window
    val hapticFeedback = LocalHapticFeedback.current
    val lambdaOnDownload = LocalLambdaOnDownload.current
    val bottomSheetNavigator = LocalBottomSheetNavigator.current

    val lambdaOnMoreClick: () -> Unit = {
        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        viewModel.setAppBarsVisible(true)
        onNavigateToMore(viewModel.post)
    }

    val lambdaOnShareClick: () -> Unit = {
        bottomSheetNavigator.navigate {
            it.navigate(
                HomeRoute.Share(
                    post = viewModel.post,
                    showThumbnail = false,
                )
            )
        }
    }

    LaunchedEffect(key1 = viewModel.areAppBarsVisible) {
        when {
            viewModel.areAppBarsVisible -> window.showSystemBars()
            else -> window.hideSystemBars()
        }
    }

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

    when (viewModel.post.type) {
        Post.Type.IMAGE, Post.Type.ANIMATED_GIF -> {
            ImagePost(
                onNavigateBack = onNavigateBack,
                onMoreClick = lambdaOnMoreClick,
                onShareClick = lambdaOnShareClick,
                areAppBarsVisible = viewModel.areAppBarsVisible,
                onAppBarsVisibleChange = viewModel::setAppBarsVisible,
            )
        }

        Post.Type.VIDEO, Post.Type.UGOIRA -> {
            VideoPost(
                areAppBarsVisible = viewModel.areAppBarsVisible,
                onAppBarsVisibleChange = viewModel::setAppBarsVisible,
                onNavigateBack = onNavigateBack,
                onMoreClick = lambdaOnMoreClick,
                onDownloadClick = {
                    lambdaOnDownload(viewModel.post)
                },
                onShareClick = lambdaOnShareClick,
            )
        }

        Post.Type.FLASH, Post.Type.UNSUPPORTED -> {
            UnsupportedPost(
                onNavigateBack = onNavigateBack,
                onMoreClick = lambdaOnMoreClick,
                onShareClick = lambdaOnShareClick,
            )
        }
    }
}
