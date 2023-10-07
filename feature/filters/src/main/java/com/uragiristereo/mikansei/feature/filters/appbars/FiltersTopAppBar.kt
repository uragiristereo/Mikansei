package com.uragiristereo.mikansei.feature.filters.appbars

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uragiristereo.mikansei.core.product.component.ProductTopAppBar
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.extension.backgroundElevation

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun FiltersTopAppBar(
    title: String,
    username: String,
    selectionMode: Boolean,
    onNavigateBack: () -> Unit,
    onRefresh: () -> Unit,
    onClose: () -> Unit,
    onDelete: () -> Unit,
    onSelectAll: () -> Unit,
    onSelectInverse: () -> Unit,
) {
    var dropdownExpanded by remember { mutableStateOf(false) }

    val backgroundColor by animateColorAsState(
        targetValue = MaterialTheme.colors.background.backgroundElevation(
            elevation = when {
                selectionMode -> 5.dp
                else -> 2.dp
            },
        ),
        label = "BackgroundColor",
    )

    ProductTopAppBar(
        backgroundColor = backgroundColor,
        title = {
            Column {
                Text(text = title)

                AnimatedContent(
                    targetState = selectionMode,
                    label = "SelectionMode",
                ) { state ->
                    if (!state) {
                        Text(
                            text = username,
                            fontSize = 14.sp,
                            color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium),
                            modifier = Modifier.animateContentSize(),
                        )
                    }
                }
            }
        },

        navigationIcon = {
            AnimatedContent(
                targetState = selectionMode,
                transitionSpec = {
                    scaleIn() with scaleOut()
                },
                label = "CloseBackButton",
            ) { state ->
                when {
                    state ->
                        IconButton(
                            onClick = onClose,
                            content = {
                                Icon(
                                    painter = painterResource(id = R.drawable.close),
                                    contentDescription = null,
                                )
                            },
                        )

                    else ->
                        IconButton(
                            onClick = onNavigateBack,
                            content = {
                                Icon(
                                    painter = painterResource(id = R.drawable.arrow_back),
                                    contentDescription = null,
                                )
                            },
                        )
                }
            }
        },
        actions = {
            IconButton(
                onClick = { dropdownExpanded = !dropdownExpanded },
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.more_vert),
                        contentDescription = null,
                    )
                },
            )

            DropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = {
                    dropdownExpanded = false
                },
                content = {
                    if (!selectionMode) {
                        DropdownMenuItem(
                            onClick = {
                                dropdownExpanded = false
                                onRefresh()
                            },
                            content = {
                                Text(text = stringResource(id = R.string.refresh))
                            },
                        )

                        Divider()
                    }

                    if (selectionMode) {
                        DropdownMenuItem(
                            onClick = {
                                dropdownExpanded = false
                                onDelete()
                            },
                            content = {
                                Text(text = stringResource(id = R.string.delete))
                            },
                        )
                    }

                    DropdownMenuItem(
                        onClick = {
                            dropdownExpanded = false
                            onSelectAll()
                        },
                        content = {
                            Text(text = stringResource(id = R.string.select_all))
                        },
                    )

                    if (selectionMode) {
                        DropdownMenuItem(
                            onClick = {
                                dropdownExpanded = false
                                onSelectInverse()
                            },
                            content = {
                                Text(text = stringResource(id = R.string.select_inverse))
                            },
                        )
                    }
                },
            )
        },
    )
}
