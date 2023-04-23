package com.uragiristereo.mikansei.feature.home.favorites.add_to_fav_group.core

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
internal fun CenterText(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        color = LocalContentColor.current.copy(alpha = ContentAlpha.medium),
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp)
            .padding(top = 8.dp),
    )
}
