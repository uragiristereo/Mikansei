package com.uragiristereo.mikansei.feature.image.image

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.google.accompanist.insets.ui.Scaffold
import com.uragiristereo.mikansei.core.model.preferences.user.DetailSizePreference
import com.uragiristereo.mikansei.core.ui.LocalLambdaOnDownload
import com.uragiristereo.mikansei.core.ui.LocalScaffoldState
import com.uragiristereo.mikansei.core.ui.LocalWindowSizeHorizontal
import com.uragiristereo.mikansei.core.ui.WindowSize
import com.uragiristereo.mikansei.feature.image.core.verticallyDraggable
import com.uragiristereo.mikansei.feature.image.image.appbars.ImageBottomAppBar
import com.uragiristereo.mikansei.feature.image.image.appbars.ImageTopAppBar
import com.uragiristereo.mikansei.feature.image.image.core.ImageLoadingState
import com.uragiristereo.mikansei.feature.image.image.core.rememberImageDoubleClickListener
import com.uragiristereo.mikansei.feature.image.image.core.rememberImageZoomSpec
import kotlinx.coroutines.flow.first
import me.saket.telephoto.zoomable.rememberZoomableImageState
import me.saket.telephoto.zoomable.rememberZoomableState
import timber.log.Timber
import kotlin.math.abs

@Composable
internal fun ImagePost(
    areAppBarsVisible: Boolean,
    gesturesEnabled: Boolean,
    isActivePage: Boolean,
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
    val activeUser by viewModel.activeUser.collectAsState()

    val post = remember { viewModel.post }

    val areAppBarsVisible by rememberUpdatedState(areAppBarsVisible)
    val onAppBarsVisibleChange by rememberUpdatedState(onAppBarsVisibleChange)
    val onMoreClick by rememberUpdatedState(onMoreClick)

    val zoomableState = rememberZoomableState(zoomSpec = rememberImageZoomSpec())
    val zoomableImageState = rememberZoomableImageState(zoomableState)
    val onDoubleClick = rememberImageDoubleClickListener()

    val expandLoadingVisible =
        viewModel.loadingState == ImageLoadingState.FROM_EXPAND && post.medias.hasScaled

    var previewLoaded by remember { mutableStateOf(false) }
    var previewDisplayed by remember { mutableStateOf(false) }
    var expandRequested by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = zoomableImageState, key2 = previewLoaded) {
        if (previewLoaded) {
            snapshotFlow { zoomableImageState.isImageDisplayed }.first { it }

            previewDisplayed = true
        }
    }

    val imageRequestBuilder = remember(viewModel) {
        ImageRequest.Builder(context)
            .decoderFactory(
                when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> ImageDecoderDecoder.Factory()
                    else -> GifDecoder.Factory()
                }
            )
    }

    val originalImageRequest = remember(viewModel) {
        imageRequestBuilder
            .data(post.medias.original.url)
            .placeholderMemoryCacheKey(
                when (activeUser.danbooru.defaultImageSize) {
                    DetailSizePreference.ORIGINAL -> "preview#${post.id}"
                    DetailSizePreference.COMPRESSED -> "scaled#${post.id}"
                }
            )
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
            .placeholderMemoryCacheKey("preview#${post.id}")
            .memoryCacheKey("scaled#${post.id}")
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
            .memoryCacheKey("preview#${post.id}")
            .listener(
                onSuccess = { _, _ ->
                    viewModel.loadingState = ImageLoadingState.DISABLED

                    previewLoaded = true
                },
            )
            .build()
    }

    val model = when {
        !previewDisplayed -> previewImageRequest
        expandRequested -> originalImageRequest
        post.medias.hasScaled && activeUser.danbooru.defaultImageSize == DetailSizePreference.ORIGINAL -> originalImageRequest
        post.medias.hasScaled -> scaledImageRequest
        else -> originalImageRequest
    }

    val lambdaOnExpandClick: () -> Unit = {
        expandRequested = true
    }

    LaunchedEffect(key1 = zoomableState, key2 = isActivePage) {
        if (isActivePage) {
            snapshotFlow { zoomableState.zoomFraction }.collect { zoomFraction ->
                onZoomChange(zoomFraction ?: 0f)
            }
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
            model = model,
            state = zoomableImageState,
            onDoubleClick = onDoubleClick,
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
