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
            AnimatedContent(
                targetState = isDeleteButton,
                transitionSpec = {
                    scaleIn() with scaleOut()
                },
                modifier = modifier.navigationBarsPadding(),
            ) { state ->
                when {
                    state ->
                        FloatingActionButton(
                            onClick = onDelete,
                            content = {
                                Icon(
                                    painter = painterResource(id = R.drawable.delete),
                                    contentDescription = null,
                                )
                            },
                        )

                    else ->
                        FloatingActionButton(
                            onClick = onAdd,
                            content = {
                                Icon(
                                    painter = painterResource(id = R.drawable.add),
                                    contentDescription = null,
                                )
                            },
                        )
                }
            }
        },
    )
}
