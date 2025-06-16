package com.uragiristereo.mikansei.feature.image.image

import android.graphics.drawable.Animatable
import android.os.Build
import android.os.Parcelable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.imageLoader
import coil.request.Disposable
import coil.request.ImageRequest
import com.google.accompanist.insets.ui.Scaffold
import com.ortiz.touchview.OnTouchImageViewListener
import com.ortiz.touchview.TouchImageView
import com.uragiristereo.mikansei.core.model.preferences.user.DetailSizePreference
import com.uragiristereo.mikansei.core.ui.LocalLambdaOnDownload
import com.uragiristereo.mikansei.core.ui.LocalScaffoldState
import com.uragiristereo.mikansei.core.ui.LocalWindowSizeHorizontal
import com.uragiristereo.mikansei.core.ui.WindowSize
import com.uragiristereo.mikansei.feature.image.core.verticallyDraggable
import com.uragiristereo.mikansei.feature.image.image.appbars.ImageBottomAppBar
import com.uragiristereo.mikansei.feature.image.image.appbars.ImageTopAppBar
import com.uragiristereo.mikansei.feature.image.image.core.ImageLoadingState
import timber.log.Timber
import kotlin.math.abs

@Composable
internal fun ImagePost(
    areAppBarsVisible: Boolean,
    gesturesEnabled: Boolean,
    offsetY: () -> Float,
    onOffsetYChange: (Float) -> Unit,
    onNavigateBack: (Boolean) -> Unit,
    onMoreClick: () -> Unit,
    onShareClick: () -> Unit,
    onAppBarsVisibleChange: (Boolean) -> Unit,
    onZoomChange: (Float) -> Unit,
    onPinchGestureChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ImageViewModel,
) {
    val context = LocalContext.current
    val lambdaOnDownload = LocalLambdaOnDownload.current

    val windowSizeHorizontal = LocalWindowSizeHorizontal.current

    val post = remember { viewModel.post }
    var imageViewState by rememberSaveable {
        mutableStateOf<Parcelable?>(null)
    }

    val imageView = remember(viewModel) {
        TouchImageView(context).apply {
            imageViewState?.let(::onRestoreInstanceState)
        }
    }

    var imageDisposable: Disposable? = remember(viewModel) { null }
    val expandLoadingVisible =
        viewModel.loadingState == ImageLoadingState.FROM_EXPAND && post.medias.hasScaled

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
            .listener(
                onSuccess = { _, _ ->
                    viewModel.loadingState = ImageLoadingState.DISABLED

                    imageDisposable = context.imageLoader.enqueue(
                        request = when {
                            post.medias.hasScaled && viewModel.activeUser.value.danbooru.defaultImageSize == DetailSizePreference.ORIGINAL -> originalImageRequest
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
                        onZoomChange(imageView.currentZoom)
                    }
                }
            )
        }

        onDispose {
            imageDisposable?.dispose()
            imageViewState = imageView.onSaveInstanceState()
        }
    }

    LaunchedEffect(key1 = areAppBarsVisible) {
        imageView.setOnClickListener {
            onAppBarsVisibleChange(!areAppBarsVisible)
        }
    }

    Scaffold(
        scaffoldState = LocalScaffoldState.current,
        topBar = {
            AnimatedVisibility(
                visible = areAppBarsVisible,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .graphicsLayer {
                        translationY = -abs(offsetY())
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
                    onShareClick = onShareClick,
                    onMoreClick = onMoreClick,
                )
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = areAppBarsVisible && windowSizeHorizontal == WindowSize.COMPACT,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .graphicsLayer {
                        translationY = abs(offsetY())
                    },
            ) {
                ImageBottomAppBar(
                    post = post,
                    expandLoadingVisible = expandLoadingVisible,
                    expandButtonVisible = viewModel.expandButtonVisible,
                    onExpandClick = lambdaOnExpandClick,
                    onDownloadClick = lambdaOnDownload,
                    onShareClick = onShareClick,
                )
            }
        },
        contentPadding = PaddingValues(0.dp),
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
                    translationY = offsetY()
                }
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            val activePointers = event.changes.count { it.pressed }
                            onPinchGestureChange(activePointers >= 2)
                        }
                    }
                }
                .verticallyDraggable(
                    enabled = gesturesEnabled,
                    offsetY = offsetY,
                    onOffsetYChange = onOffsetYChange,
                    onDragExit = {
                        onAppBarsVisibleChange(true)
                        onNavigateBack(true)
                    },
                ),
        )
    }
}
