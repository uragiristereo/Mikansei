package com.uragiristereo.mikansei.feature.image.image

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import coil.decode.ImageDecoderDecoder
import coil.imageLoader
import coil.request.Disposable
import coil.request.ImageRequest
import com.ortiz.touchview.OnTouchImageViewListener
import com.ortiz.touchview.TouchImageView
import com.uragiristereo.mikansei.core.model.danbooru.post.Post
import com.uragiristereo.mikansei.core.preferences.model.DetailSizePreference
import com.uragiristereo.mikansei.feature.image.ImageViewModel
import com.uragiristereo.mikansei.feature.image.core.ImageLoadingState
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.math.abs

@Composable
internal fun ImagePost(
    post: Post,
    maxOffset: Float,
    onNavigateBack: (Boolean) -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ImageViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val imageView = remember(viewModel) { TouchImageView(context) }
    var imageDisposable: Disposable? = remember(viewModel) { null }

    val imageRequest = remember(viewModel) {
        ImageRequest.Builder(context)
            .listener(
                onStart = {
                    viewModel.loading = when {
                        viewModel.originalImageShown -> ImageLoadingState.FROM_EXPAND
                        else -> ImageLoadingState.FROM_LOAD
                    }
                },
                onSuccess = { _, _ ->
                    viewModel.loading = ImageLoadingState.DISABLED
                },
            )
            .decoderFactory(ImageDecoderDecoder.Factory())
    }

    DisposableEffect(key1 = viewModel) {
        val resized = viewModel.resizeImage(post.image.width, post.image.height)

        imageDisposable = context.imageLoader.enqueue(
            request = imageRequest
                .data(
                    data = when {
                        viewModel.detailSize == DetailSizePreference.ORIGINAL -> post.image.url
                        post.hasScaled -> post.scaledImage.url
                        else -> post.image.url
                    }
                )
                .target(imageView)
                .size(
                    width = resized.first,
                    height = resized.second,
                )
                .build()
        )

        imageView.apply {
            maxZoom = 5f
            doubleTapScale = 2f

            setOnClickListener {
                viewModel.appBarsVisible = !viewModel.appBarsVisible
            }

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

    LaunchedEffect(key1 = viewModel.isPressed) {
        if (!viewModel.isPressed) {
            if (abs(viewModel.offsetY.value) >= maxOffset * 0.7) {
                viewModel.appBarsVisible = true

                onNavigateBack(true)
            } else {
                viewModel.offsetY.animateTo(targetValue = 0f)
            }
        }
    }

    LaunchedEffect(key1 = viewModel.originalImageShown) {
        if (viewModel.originalImageShown) {
            val resized = viewModel.resizeImage(post.image.width, post.image.height)

            viewModel.loading = ImageLoadingState.FROM_EXPAND

            imageDisposable?.dispose()

            imageDisposable = context.imageLoader.enqueue(
                request = imageRequest
                    .data(post.image.url)
                    .size(
                        width = resized.first,
                        height = resized.second,
                    )
                    .target(
                        onSuccess = { drawable ->
                            imageView.setImageDrawable(drawable)
                        },
                    )
                    .build()
            )
        }
    }

    ImageViewer(
        imageView = imageView,
        loading = viewModel.loading == ImageLoadingState.FROM_LOAD,
        modifier = modifier
            .graphicsLayer {
                translationY = viewModel.offsetY.value
            }
            .pointerInput(key1 = Unit) {
                detectTapGestures(
                    onLongPress = {
                        onMoreClick()
                    },
                    onTap = {
                        viewModel.appBarsVisible = !viewModel.appBarsVisible
                    },
                )
            }
            .pointerInput(key1 = Unit) {
                val coroutineContext = currentCoroutineContext()

                awaitPointerEventScope {
                    do {
                        val event = awaitPointerEvent()

                        viewModel.fingerCount = event.changes.size
                    } while (
                        event.changes.any { it.pressed } && coroutineContext.isActive
                    )
                }
            }
            .pointerInput(
                key1 = viewModel.currentZoom,
                key2 = viewModel.fingerCount,
            ) {
                if (viewModel.offsetY.value != 0f && viewModel.fingerCount > 1) {
                    viewModel.isPressed = false
                }

//                Timber.d("currentZoom ${viewModel.currentZoom}")

                if (imageView.currentZoom == 1f) {
                    detectDragGestures(
                        onDragEnd = {
                            scope.launch {
                                delay(timeMillis = 50L)

                                if (abs(viewModel.offsetY.value) >= maxOffset * 0.7) {
                                    viewModel.appBarsVisible = true
                                    onNavigateBack(true)
                                } else {
                                    viewModel.offsetY.animateTo(targetValue = 0f)
                                }
                            }
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()

                            val deceleratedDragAmount = dragAmount.y * 0.7f

                            if (abs(x = viewModel.offsetY.value + deceleratedDragAmount) <= maxOffset) {
                                scope.launch {
                                    viewModel.offsetY.snapTo(targetValue = viewModel.offsetY.value + deceleratedDragAmount)
                                }
                            }
                        },
                    )
                }
            },
    )
}