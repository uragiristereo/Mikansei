package com.uragiristereo.mikansei.feature.home.favorites.favgroup.new.core

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NewFavGroupFab(
    state: FabState,
    onSubmitFabClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(
        targetState = state,
        transitionSpec = {
            scaleIn() togetherWith scaleOut()
        },
        label = "NewFavGroupFab",
        modifier = modifier,
    ) { fabState ->
        when (fabState) {
            FabState.ENABLED -> SubmitFab(onClick = onSubmitFabClick)
            FabState.LOADING -> LoadingFab()
            FabState.HIDDEN -> Spacer(modifier = Modifier.size(56.dp))
        }
    }
}
