package com.uragiristereo.mikansei.feature.image.image

import android.graphics.drawable.Animatable
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.imageLoader
import coil.request.Disposable
import coil.request.ImageRequest
import com.google.accompanist.insets.ui.Scaffold
import com.ortiz.touchview.OnTouchImageViewListener
import com.ortiz.touchview.TouchImageView
import com.uragiristereo.mikansei.core.model.danbooru.ShareOption
import com.uragiristereo.mikansei.core.model.preferences.user.DetailSizePreference
import com.uragiristereo.mikansei.core.ui.LocalLambdaOnDownload
import com.uragiristereo.mikansei.core.ui.LocalLambdaOnShare
import com.uragiristereo.mikansei.core.ui.WindowSize
import com.uragiristereo.mikansei.core.ui.rememberWindowSize
import com.uragiristereo.mikansei.feature.image.core.verticallyDraggable
import com.uragiristereo.mikansei.feature.image.image.appbars.ImageBottomAppBar
import com.uragiristereo.mikansei.feature.image.image.appbars.ImageTopAppBar
import com.uragiristereo.mikansei.feature.image.image.core.ImageLoadingState
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber
import kotlin.math.abs

@Composable
internal fun ImagePost(
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

    val windowSize = rememberWindowSize()

    val post = remember { viewModel.post }
    val imageView = remember(viewModel) { TouchImageView(context) }
    var imageDisposable: Disposable? = remember(viewModel) { null }
    val expandLoadingVisible = viewModel.loadingState == ImageLoadingState.FROM_EXPAND && post.medias.hasScaled

    val imageRequestBuilder = remember(viewModel) {
        val resizedImageSize = viewModel.resizeImage(
            width = post.medias.original.width,
            height = post.medias.original.height
        )

        ImageRequest.Builder(context)
            .size(
                width = resizedImageSize.first,
                height = resizedImageSize.second,
            )
            .decoderFactory(
                when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> ImageDecoderDecoder.Factory()
                    else -> GifDecoder.Factory()
                }
            )
            .target(
                onSuccess = { drawable ->
                    Timber.d("success loading image")
                    imageView.setImageDrawable(drawable)

                    val animatable = drawable as? Animatable
                    animatable?.start()
                },
            )
    }

    val originalImageRequest = remember(viewModel) {
        imageRequestBuilder
            .data(post.medias.original.url)
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
            .data(post.medias.scaled?.url)
            .listener(
                onSuccess = { _, _ ->
                    Timber.d("scaled image loaded")
                },
            )
            .build()
    }

    val previewImageRequest = remember(viewModel) {
        imageRequestBuilder
            .data(post.medias.preview.url)
            .size(
                width = post.medias.original.width,
                height = post.medias.original.height,
            )
            .listener(
                onSuccess = { _, _ ->
                    viewModel.loadingState = ImageLoadingState.DISABLED

                    imageDisposable = context.imageLoader.enqueue(
                        request = when {
                            post.medias.hasScaled && viewModel.activeUser.danbooru.defaultImageSize == DetailSizePreference.ORIGINAL -> originalImageRequest
                            post.medias.hasScaled -> scaledImageRequest
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
                .verticallyDraggable(
                    key1 = viewModel.currentZoom,
                    offsetY = viewModel.offsetY,
                    onDragExit = {
                        onAppBarsVisibleChange(true)
                        onNavigateBack(true)
                    },
                ),
        )
    }
}
