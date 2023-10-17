package com.uragiristereo.mikansei.feature.home.favorites.new_fav_group.core

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.with
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun NewFavGroupFab(
    isLoading: Boolean,
    onSubmitFabClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(
        targetState = isLoading,
        transitionSpec = {
            scaleIn() with scaleOut()
        },
        label = "SubmitLoadingFab",
        modifier = modifier,
    ) { state ->
        when {
            !state -> SubmitFab(onClick = onSubmitFabClick)

            else -> LoadingFab()
        }
    }
}
