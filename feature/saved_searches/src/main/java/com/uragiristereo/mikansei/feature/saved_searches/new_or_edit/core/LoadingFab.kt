package com.uragiristereo.mikansei.feature.saved_searches.new_or_edit.core

import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoadingFab(
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        onClick = { },
        content = {
            CircularProgressIndicator(
                color = LocalContentColor.current,
                strokeWidth = 2.dp,
                modifier = Modifier.size(24.dp),
            )
        },
        modifier = modifier,
    )
}
