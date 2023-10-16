package com.uragiristereo.mikansei.feature.home.favorites.add_to_fav_group.core

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun CreateNewFavGroupButton(
    postId: Int,
    enabled: Boolean,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        TextButton(
            enabled = enabled,
            onClick = {
                onClick(postId)
            },
            content = {
                Text(text = "New Favorite Group".uppercase())
            },
        )
    }
}
