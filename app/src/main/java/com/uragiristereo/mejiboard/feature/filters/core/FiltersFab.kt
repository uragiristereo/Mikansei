package com.uragiristereo.mejiboard.feature.filters.core

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.uragiristereo.mejiboard.R

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FiltersFab(
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
