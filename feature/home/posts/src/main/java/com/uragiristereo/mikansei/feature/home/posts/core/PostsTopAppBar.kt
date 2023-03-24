package com.uragiristereo.mikansei.feature.home.posts.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.uragiristereo.mikansei.core.product.component.ProductTopAppBar
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.composable.DimensionSubcomposeLayout
import com.uragiristereo.mikansei.core.ui.extension.backgroundElevation

@Composable
internal fun PostsTopAppBar(
    searchTags: String,
    onHeightChange: (Dp) -> Unit,
    onSearchClick: (String) -> Unit,
    onRefreshClick: () -> Unit,
    onExitClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var dropdownExpanded by rememberSaveable { mutableStateOf(false) }

    DimensionSubcomposeLayout(modifier = modifier) {
        LaunchedEffect(key1 = size) {
            onHeightChange(size.height)
        }

        Column(
            modifier = Modifier.background(color = MaterialTheme.colors.background),
        ) {
            ProductTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                actions = {
                    IconButton(
                        onClick = {
                            dropdownExpanded = false
                            onSearchClick(searchTags)
                        },
                        content = {
                            Icon(
                                painter = painterResource(id = R.drawable.search),
                                contentDescription = null,
                            )
                        },
                    )

                    IconButton(
                        onClick = { dropdownExpanded = true },
                        content = {
                            Icon(
                                painter = painterResource(id = R.drawable.more_vert),
                                contentDescription = null,
                            )
                        },
                    )

                    PostsTopAppBarDropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false },
                        onRefreshClick = {
                            dropdownExpanded = false
                            onRefreshClick()
                        },
                        onExitClick = {
                            dropdownExpanded = false
                            onExitClick()
                        },
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
