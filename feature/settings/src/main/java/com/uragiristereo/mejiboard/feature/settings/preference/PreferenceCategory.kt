package com.uragiristereo.mejiboard.feature.settings.preference

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PreferenceCategory(
    title: String,
    modifier: Modifier = Modifier,
) {
    val iconPadding = LocalIconPadding.current

    Text(
        text = title.uppercase(),
        modifier = modifier
            .padding(
                start = when {
                    iconPadding -> 72.dp
                    else -> 16.dp
                },
                top = 24.dp,
                end = 16.dp,
                bottom = 8.dp,
            ),
        color = MaterialTheme.colors.primary,
        style = MaterialTheme.typography.overline,
        fontSize = 12.sp,
    )
}
