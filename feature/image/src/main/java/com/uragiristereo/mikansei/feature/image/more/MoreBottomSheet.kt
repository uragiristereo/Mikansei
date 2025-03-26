package com.uragiristereo.mikansei.feature.image.more

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsIgnoringVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.model.preferences.user.RatingPreference
import com.uragiristereo.mikansei.core.product.shared.postfavoritevote.PostFavoriteVote
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.LocalLambdaOnDownload
import com.uragiristereo.mikansei.core.ui.LocalSnackbarHostState
import com.uragiristereo.mikansei.core.ui.LocalWindowSizeHorizontal
import com.uragiristereo.mikansei.core.ui.WindowSize
import com.uragiristereo.mikansei.core.ui.composable.OverlappingLayout
import com.uragiristereo.mikansei.core.ui.composable.SetSystemBarsColors
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator.LocalBottomSheetContentPadding
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator.bottomSheetContentPadding
import com.uragiristereo.mikansei.feature.image.more.core.MoreActionsRow
import com.uragiristereo.mikansei.feature.image.more.core.MoreCloseButton
import com.uragiristereo.mikansei.feature.image.more.core.MoreDate
import com.uragiristereo.mikansei.feature.image.more.core.MoreSource
import com.uragiristereo.mikansei.feature.image.more.core.MoreTagsButton
import com.uragiristereo.mikansei.feature.image.more.info.MoreInfoColumn
import com.uragiristereo.mikansei.feature.image.more.tags.MoreTagsRow
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun MoreBottomSheet(
    showExpandButton: Boolean,
    onDismiss: suspend () -> Unit,
    onExpandClick: () -> Unit,
    onAddToClick: (Post) -> Unit,
    onShareClick: (Post) -> Unit,
    viewModel: MoreBottomSheetViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val lambdaOnDownload = LocalLambdaOnDownload.current
    val snackbarHostState = LocalSnackbarHostState.current

    val scope = rememberCoroutineScope()
    val windowSizeHorizontal = LocalWindowSizeHorizontal.current

    val tagsCount = remember(viewModel.post) { viewModel.post.tags.size }
    val activeUser by viewModel.activeUser.collectAsState()

    val closeButtonVisible by remember {
        derivedStateOf {
            viewModel.tagsExpanded && windowSizeHorizontal == WindowSize.COMPACT && viewModel.tags.isNotEmpty()
        }
    }

    val isInSafeMode = remember(activeUser) {
        activeUser.danbooru.safeMode || activeUser.mikansei.postsRatingFilter == RatingPreference.GENERAL_ONLY
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.postFavoriteSnackbarEvent.collect { event ->
            launch(SupervisorJob()) {
                when (event) {
                    PostFavoriteVote.Event.LOGIN_REQUIRED -> {
                        onDismiss()
                        snackbarHostState.showSnackbar(
                            message = context.getString(R.string.please_login)
                        )
                    }
                }
            }
        }
    }

    SetSystemBarsColors(
        statusBarColor = Color.Transparent,
        navigationBarColor = Color.Transparent,
        statusBarDarkIcons = false,
        navigationBarDarkIcons = MaterialTheme.colors.isLight,
    )

    OverlappingLayout(
        contentPadding = LocalBottomSheetContentPadding.current,
        overlapContent = {
            MoreCloseButton(
                visible = closeButtonVisible,
                onClick = {
                    scope.launch {
                        onDismiss()
                    }
                },
            )
        },
        modifier = Modifier.animateContentSize(),
    ) { closeButtonPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .bottomSheetContentPadding()
                .padding(closeButtonPadding),
        ) {
            MoreActionsRow(
                showExpandButton = showExpandButton,
                onDownloadClick = {
                    scope.launch {
                        onDismiss()
                        lambdaOnDownload(viewModel.post)
                    }
                },
                onShareClick = {
                    onShareClick(viewModel.post)
                },
                onExpandClick = {
                    scope.launch {
                        onDismiss()
                        onExpandClick()
                    }
                },
                onOpenInExternalClick = {
                    viewModel.launchUrl(context, viewModel.postLink)
                },
                onAddToClick = {
                    scope.launch(SupervisorJob()) {
                        if (viewModel.activeUser.value.isNotAnonymous()) {
                            onAddToClick(viewModel.post)
                        } else {
                            onDismiss()
                            snackbarHostState.showSnackbar(
                                message = context.getString(R.string.please_login),
                            )
                        }
                    }
                },
                favoriteCount = viewModel.favoriteCount,
                isOnFavorite = viewModel.isPostInFavorites,
                onToggleFavorite = viewModel::toggleFavorite,
                favoriteButtonEnabled = viewModel.favoriteButtonEnabled && viewModel.isPostUpdated,
                score = viewModel.score,
                scoreState = viewModel.scoreState,
                voteButtonEnabled = viewModel.voteButtonEnabled && viewModel.isPostUpdated,
                onVoteChange = viewModel::onVoteChange,
                modifier = Modifier.padding(bottom = 8.dp),
            )

            Divider()

            Text(
                text = stringResource(id = R.string.image_information),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp,
                        bottom = 8.dp,
                    ),
            )

            MoreDate(date = viewModel.post.createdAt)

            MoreSource(
                source = viewModel.post.source,
                onClick = { source ->
                    viewModel.launchUrl(context, source)
                },
                onLongClick = { source ->
                    viewModel.copyToClipboard(context, source)
                },
            )

            MoreInfoColumn(
                post = viewModel.post,
                scaledImageFileSizeStr = viewModel.scaledImageFileSizeStr,
                originalImageFileSizeStr = viewModel.originalImageFileSizeStr,
                expanded = viewModel.infoExpanded,
                uploaderName = viewModel.uploaderName,
                shouldShowRating = !isInSafeMode,
                onMoreClick = {
                    viewModel.infoExpanded = true
                    if (viewModel.originalImageFileSizeStr.isEmpty() || viewModel.originalImageFileSizeStr.isEmpty()) {
                        viewModel.getImagesFileSize()
                    }
                },
            )

            Divider()

            Box {
                if (viewModel.loading) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 8.dp)
                            .windowInsetsPadding(WindowInsets.navigationBarsIgnoringVisibility),
                        content = {
                            CircularProgressIndicator()
                        },
                    )
                }

                if (!viewModel.tagsExpanded && !viewModel.loading) {
                    MoreTagsButton(
                        tagCount = tagsCount,
                        onClick = {
                            viewModel.getTags()
                        },
                        modifier = Modifier
                            .windowInsetsPadding(WindowInsets.navigationBarsIgnoringVisibility),
                    )
                }

                if (viewModel.tagsExpanded && viewModel.tags.isNotEmpty()) {
                    MoreTagsRow(
                        tags = viewModel.tags,
                        tagsCount = tagsCount,
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .windowInsetsPadding(WindowInsets.navigationBarsIgnoringVisibility),
                    )
                }
            }
        }
    }
}
