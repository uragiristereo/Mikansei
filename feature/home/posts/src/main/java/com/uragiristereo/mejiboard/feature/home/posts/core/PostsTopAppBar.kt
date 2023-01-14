package com.uragiristereo.mejiboard.feature.home.posts.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uragiristereo.mejiboard.core.common.ui.composable.DimensionLayout
import com.uragiristereo.mejiboard.core.common.ui.extension.backgroundElevation
import com.uragiristereo.mejiboard.core.product.component.ProductTopAppBar
import com.uragiristereo.mejiboard.core.resources.R

@Composable
fun PostsTopAppBar(
    searchTags: String,
    booruSource: String,
    dropdownExpanded: Boolean,
    onDropdownDismiss: () -> Unit,
    onHeightChange: (Dp) -> Unit,
    onSearchClick: () -> Unit,
    onMoreClick: () -> Unit,
    onRefreshClick: () -> Unit,
    onExitClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DimensionLayout(modifier = modifier) {
        LaunchedEffect(key1 = size) {
            onHeightChange(size.height)
        }

        Column(
            modifier = Modifier.background(color = MaterialTheme.colors.background),
        ) {
            ProductTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column {
                            Text(text = stringResource(id = R.string.app_name_alt))

                            Text(
                                text = booruSource,
                                fontSize = 14.sp,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.74f),
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = onSearchClick,
                        content = {
                            Icon(
                                painter = painterResource(id = R.drawable.search),
                                contentDescription = null,
                            )
                        },
                    )

                    IconButton(
                        onClick = onMoreClick,
                        content = {
                            Icon(
                                painter = painterResource(id = R.drawable.more_vert),
                                contentDescription = null,
                            )
                        },
                    )

                    PostsTopAppBarDropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = onDropdownDismiss,
                        onRefreshClick = onRefreshClick,
                        onExitClick = onExitClick,
                    )
                },
            )

            Column {
                Text(
                    text = buildAnnotatedString {
                        append("${stringResource(id = R.string.browse)} ")

                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            when {
                                searchTags.isEmpty() -> append(stringResource(id = R.string.all_posts))
                                else -> append(searchTags)
                            }
                        }
                    },
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 8.dp,
                            vertical = 4.dp,
                        ),
                )

                Divider()
            }
        }
    }
}

@Composable
private fun PostsTopAppBarDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onRefreshClick: () -> Unit,
    onExitClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier
            .background(MaterialTheme.colors.background.backgroundElevation()),
        content = {
            DropdownMenuItem(
                onClick = onRefreshClick,
                content = {
                    Text(text = stringResource(id = R.string.refresh))
                },
            )

            Divider()

            DropdownMenuItem(
                onClick = onExitClick,
                content = {
                    Text(text = stringResource(id = R.string.exit_app))
                },
            )
        },
    )
}
