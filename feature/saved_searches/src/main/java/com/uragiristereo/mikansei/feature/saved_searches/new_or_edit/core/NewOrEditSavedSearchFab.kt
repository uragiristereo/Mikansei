package com.uragiristereo.mikansei.feature.saved_searches.new_or_edit.core

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
fun NewOrEditSavedSearchFab(
    state: FabState,
    onSubmitFabClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(
        targetState = state,
        transitionSpec = {
            scaleIn() togetherWith scaleOut()
        },
        label = "NewOrEditSavedSearchFab",
        modifier = modifier,
    ) { fabState ->
        when (fabState) {
            FabState.ENABLED -> SubmitFab(onClick = onSubmitFabClick)
            FabState.LOADING -> LoadingFab()
            FabState.HIDDEN -> Spacer(modifier = Modifier.size(56.dp))
        }
    }
}
