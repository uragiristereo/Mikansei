package com.uragiristereo.mikansei.feature.filters.core

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.uragiristereo.mikansei.core.resources.R

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun FiltersFab(
    visible: Boolean,
    isDeleteButton: Boolean,
    onAdd: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
        content = {
            FloatingActionButton(
                onClick = when {
                    isDeleteButton -> onDelete
                    else -> onAdd
                },
                content = {
                    AnimatedContent(
                        targetState = isDeleteButton,
                        content = { notEmpty ->
                            when {
                                notEmpty -> Icon(
                                    painter = painterResource(id = R.drawable.delete),
                                    contentDescription = null,
                                )

                                else -> Icon(
                                    painter = painterResource(id = R.drawable.add),
                                    contentDescription = null,
                                )
                            }
                        },
                    )
                },
                modifier = modifier
                    .windowInsetsPadding(
                        insets = WindowInsets.navigationBars.only(sides = WindowInsetsSides.Bottom),
                    ),
            )
        },
    )
}
