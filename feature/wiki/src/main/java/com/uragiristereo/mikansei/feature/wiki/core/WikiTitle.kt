package com.uragiristereo.mikansei.feature.wiki.core

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WikiTitle(
    title: String,
    color: Color?,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        color = color ?: Color.Unspecified,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp,
            ),
    )
}
