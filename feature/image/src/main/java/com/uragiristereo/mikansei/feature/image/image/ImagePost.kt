package com.uragiristereo.mikansei.feature.image.image

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import coil.imageLoader
import coil.request.Disposable
import coil.request.ImageRequest
import com.google.accompanist.insets.ui.Scaffold
import com.ortiz.touchview.OnTouchImageViewListener
import com.ortiz.touchview.TouchImageView
import com.uragiristereo.mikansei.core.model.ShareOption
import com.uragiristereo.mikansei.core.model.user.preference.DetailSizePreference
import com.uragiristereo.mikansei.core.ui.LocalLambdaOnDownload
import com.uragiristereo.mikansei.core.ui.LocalLambdaOnShare
import com.uragiristereo.mikansei.core.ui.WindowSize
import com.uragiristereo.mikansei.core.ui.rememberWindowSize
import com.uragiristereo.mikansei.feature.image.image.appbars.ImageBottomAppBar
import com.uragiristereo.mikansei.feature.image.image.appbars.ImageTopAppBar
import com.uragiristereo.mikansei.feature.image.image.core.ImageLoadingState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber
import kotlin.math.abs

@Composable
internal fun ImagePost(
    maxOffset: Float,
    onNavigateBack: (Boolean) -> Unit,
    onMoreClick: () -> Unit,
    areAppBarsVisible: Boolean,
    onAppBarsVisibleChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ImageViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val lambdaOnDownload = LocalLambdaOnDownload.current
    val lambdaOnShare = LocalLambdaOnShare.current

    val scope = rememberCoroutineScope()
    val windowSize = rememberWindowSize()

    val post = remember { viewModel.post }
    val imageView = remember(viewModel) { TouchImageView(context) }
    var imageDisposable: Disposable? = remember(viewModel) { null }
    val expandLoadingVisible = viewModel.loadingState == ImageLoadingState.FROM_EXPAND && post.hasScaled

    val imageRequestBuilder = remember(viewModel) {
        val resizedImageSize = viewModel.resizeImage(
            width = post.image.width,
            height = post.image.height
        )

        ImageRequest.Builder(context)
            .size(
                width = resizedImageSize.first,
                height = resizedImageSize.second,
            )
            .target(
                onSuccess = { drawable ->
                    Timber.d("success loading image")
                    imageView.setImageDrawable(drawable)
                },
            )
    }

    val originalImageRequest = remember(viewModel) {
        imageRequestBuilder
            .data(post.image.url)
            .listener(
                onStart = {
                    viewModel.onExpandImage()
                },
                onSuccess = { _, _ ->
                    viewModel.loadingState = ImageLoadingState.DISABLED

                    Timber.d("original image loaded")
                },
            )
            .build()
    }

    val scaledImageRequest = remember(viewModel) {
        imageRequestBuilder
            .data(post.scaledImage.url)
            .listener(
                onSuccess = { _, _ ->
                    Timber.d("scaled image loaded")
                },
            )
            .build()
    }

    val previewImageRequest = remember(viewModel) {
        imageRequestBuilder
            .data(post.previewImage.url)
            .size(
                width = post.scaledImage.width,
                height = post.scaledImage.height,
            )
            .listener(
                onSuccess = { _, _ ->
                    viewModel.loadingState = ImageLoadingState.DISABLED

                    imageDisposable = context.imageLoader.enqueue(
                        request = when {
                            post.hasScaled && viewModel.activeUser.defaultImageSize == DetailSizePreference.ORIGINAL -> originalImageRequest
                            post.hasScaled -> scaledImageRequest
                            else -> originalImageRequest
                        }
                    )
                },
            )
            .build()
    }

    val lambdaOnExpandClick: () -> Unit = {
        imageDisposable?.dispose()
        imageDisposable = context.imageLoader.enqueue(originalImageRequest)
    }

    DisposableEffect(key1 = viewModel) {
        context.imageLoader.enqueue(previewImageRequest)

        imageView.apply {
            maxZoom = 5f
            doubleTapScale = 2f

            setOnLongClickListener {
                onMoreClick()
                false
            }

            setOnTouchImageViewListener(
                object : OnTouchImageViewListener {
                    override fun onMove() {
                        viewModel.currentZoom = imageView.currentZoom
                    }
                }
            )
        }

        onDispose {
            imageDisposable?.dispose()
        }
    }

    LaunchedEffect(key1 = areAppBarsVisible) {
        imageView.setOnClickListener {
            onAppBarsVisibleChange(!areAppBarsVisible)
        }
    }

    Scaffold(
        topBar = {
            AnimatedVisibility(
                visible = areAppBarsVisible,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .graphicsLayer {
                        translationY = -abs(viewModel.offsetY.value)
                    },
            ) {
                ImageTopAppBar(
                    post = post,
                    expandLoadingVisible = expandLoadingVisible,
                    expandButtonVisible = viewModel.expandButtonVisible,
                    onNavigateBack = {
                        onNavigateBack(/* navigatedBackByGesture = */ false)
                    },
                    onExpandClick = lambdaOnExpandClick,
                    onDownloadClick = lambdaOnDownload,
                    onShareClick = {
                        lambdaOnShare(post, ShareOption.COMPRESSED)
                    },
                    onMoreClick = onMoreClick,
                )
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = areAppBarsVisible && windowSize == WindowSize.COMPACT,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .graphicsLayer {
                        translationY = abs(viewModel.offsetY.value)
                    },
            ) {
                ImageBottomAppBar(
                    post = post,
                    expandLoadingVisible = expandLoadingVisible,
                    expandButtonVisible = viewModel.expandButtonVisible,
                    onExpandClick = lambdaOnExpandClick,
                    onDownloadClick = lambdaOnDownload,
                    onShareClick = {
                        lambdaOnShare(post, ShareOption.COMPRESSED)
                    },
                )
            }
        },
        modifier = modifier,
    ) {
        ImageViewer(
            imageView = imageView,
            loadingVisible = viewModel.loadingState == ImageLoadingState.FROM_LOAD,
            onTap = {
                onAppBarsVisibleChange(!areAppBarsVisible)
            },
            onLongPress = onMoreClick,
            modifier = Modifier
                .graphicsLayer {
                    translationY = viewModel.offsetY.value
                }
                .pointerInput(key1 = viewModel.currentZoom) {
                    if (viewModel.currentZoom == 1f) {
                        detectDragGestures(
                            onDragEnd = {
                                scope.launch {
                                    delay(timeMillis = 50L)

                                    if (abs(viewModel.offsetY.value) >= maxOffset * 0.7) {
                                        onAppBarsVisibleChange(true)
                                        onNavigateBack(true)
                                    } else {
                                        viewModel.offsetY.animateTo(targetValue = 0f)
                                    }
                                }
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()

                                val deceleratedDragAmount = dragAmount.y * 0.7f

                                if (abs(viewModel.offsetY.value + deceleratedDragAmount) <= maxOffset) {
                                    scope.launch {
                                        viewModel.offsetY.snapTo(
                                            targetValue = viewModel.offsetY.value + deceleratedDragAmount,
                                        )
                                    }
                                }
                            },
                        )
                    }
                },
        )
    }
}
