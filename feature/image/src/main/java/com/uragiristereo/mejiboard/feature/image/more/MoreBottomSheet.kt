package com.uragiristereo.mejiboard.feature.image.more

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uragiristereo.mejiboard.core.model.booru.post.Post
import com.uragiristereo.mejiboard.core.product.component.ProductModalBottomSheet
import com.uragiristereo.mejiboard.core.resources.R
import com.uragiristereo.mejiboard.core.ui.LocalLambdaOnDownload
import com.uragiristereo.mejiboard.core.ui.WindowSize
import com.uragiristereo.mejiboard.core.ui.composable.DragHandle
import com.uragiristereo.mejiboard.core.ui.composable.NavigationBarSpacer
import com.uragiristereo.mejiboard.core.ui.rememberWindowSize
import com.uragiristereo.mejiboard.feature.image.more.core.MoreActionsRow
import com.uragiristereo.mejiboard.feature.image.more.core.MoreCloseButton
import com.uragiristereo.mejiboard.feature.image.more.core.MoreTagsButton
import com.uragiristereo.mejiboard.feature.image.more.info.MoreInfoColumn
import com.uragiristereo.mejiboard.feature.image.more.tags.MoreTagsRow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.text.DateFormat

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun MoreBottomSheet(
    post: Post,
    sheetState: ModalBottomSheetState,
    showExpandButton: Boolean,
    onExpandClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MoreBottomSheetViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val lambdaOnDownload = LocalLambdaOnDownload.current

    val scope = rememberCoroutineScope()
    val columnState = rememberLazyListState()
    val windowSize = rememberWindowSize()

    val tags = remember(post) {
        post.tags.split(' ')
    }
    val tagsCount = remember(post) { tags.size }

    val closeButtonVisible by remember {
        derivedStateOf {
            viewModel.tagsExpanded && windowSize == WindowSize.COMPACT && viewModel.tags.isNotEmpty()
        }
    }

    LaunchedEffect(key1 = sheetState.currentValue) {
        if (sheetState.currentValue == ModalBottomSheetValue.Hidden) {
            viewModel.collapseAll()
        }
    }

    ProductModalBottomSheet(
        sheetState = sheetState,
        modifier = modifier,
        content = {
            Box {
                LazyColumn(
                    state = columnState,
                    contentPadding = PaddingValues(
                        top = when {
                            !closeButtonVisible -> 24.dp
                            else -> 0.dp
                        },
                    ),
                ) {
                    if (windowSize == WindowSize.COMPACT && closeButtonVisible) {
                        item {
                            Spacer(
                                modifier = Modifier.height(viewModel.closeButtonHeight),
                            )
                        }
                    }

                    item {
                        MoreActionsRow(
                            showExpandButton = showExpandButton,
                            onDownloadClick = {
                                scope.launch {
                                    sheetState.hide()

                                    lambdaOnDownload(post)
                                }
                            },
                            onShareClick = { /*TODO*/ },
                            onExpandClick = {
                                scope.launch {
                                    sheetState.hide()
                                    onExpandClick()
                                }
                            },
                            onOpenInExternalClick = remember {
                                {
                                    viewModel.launchUrl(
                                        context = context,
                                        url = post.source.parseWebUrl(post.id),
                                    )
                                }
                            },
                            modifier = Modifier.padding(bottom = 8.dp),
                        )
                    }

                    item {
                        Divider()
                    }

                    item {
                        Text(
                            text = stringResource(id = R.string.image_details),
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
                    }

                    item {
                        val df = remember { DateFormat.getDateTimeInstance() }

                        Text(
                            text = df.format(post.uploadedAt),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                    bottom = 2.dp,
                                ),
                        )
                    }

                    if (post.imageSource.isNotEmpty()) {
                        item {
                            Text(
                                text = post.imageSource,
                                fontSize = 14.sp,
                                color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(
                                        onClick = remember { { viewModel.launchUrl(context = context, url = post.imageSource) } },
                                    )
                                    .padding(
                                        horizontal = 16.dp,
                                        vertical = 4.dp,
                                    ),
                            )
                        }
                    }

                    item {
                        MoreInfoColumn(
                            post = post,
                            scaledImageFileSizeStr = viewModel.scaledImageFileSizeStr,
                            originalImageFileSizeStr = viewModel.originalImageFileSizeStr,
                            expanded = viewModel.infoExpanded,
                            onMoreClick = remember {
                                {
                                    viewModel.infoExpanded = true

                                    scope.launch {
                                        columnState.scrollToItem(index = 0)
                                    }

                                    if (viewModel.originalImageFileSizeStr.isEmpty() || viewModel.originalImageFileSizeStr.isEmpty()) {
                                        viewModel.getImagesFileSize(post)
                                    }
                                }
                            },
                        )
                    }

                    item {
                        Divider()
                    }

                    if (viewModel.loading) {
                        item {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(all = 8.dp),
                                content = {
                                    CircularProgressIndicator()
                                },
                            )
                        }
                    }

                    if (!viewModel.tagsExpanded && !viewModel.loading) {
                        item {
                            MoreTagsButton(
                                tagCount = tagsCount,
                                onClick = remember {
                                    {
                                        scope.launch {
                                            sheetState.animateTo(ModalBottomSheetValue.Expanded)
                                        }

                                        viewModel.getTags(tags)
                                    }
                                },
                            )
                        }
                    }

                    if (viewModel.tagsExpanded && viewModel.tags.isNotEmpty()) {
                        item {
                            Text(
                                text = stringResource(id = R.string.image_n_tags, tagsCount),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colors.primary,
                                modifier = Modifier
                                    .padding(
                                        top = 16.dp,
                                        bottom = 12.dp,
                                        start = 16.dp,
                                        end = 16.dp,
                                    ),
                            )
                        }

                        item {
                            MoreTagsRow(
                                tags = viewModel.tags,
                                modifier = Modifier.padding(bottom = 16.dp),
                            )
                        }
                    }

                    item {
                        NavigationBarSpacer()
                    }
                }

                MoreCloseButton(
                    visible = closeButtonVisible,
                    currentHeight = viewModel.closeButtonHeight,
                    onHeightChanged = remember { { viewModel.closeButtonHeight = it } },
                    onClick = {
                        scope.launch {
                            sheetState.animateTo(ModalBottomSheetValue.Hidden)
                        }
                    },
                )

                DragHandle()
            }
        },
    )
}
