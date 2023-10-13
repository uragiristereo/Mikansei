package com.uragiristereo.mikansei.core.ui.composable

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 16.sp,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.subtitle1,
        fontSize = fontSize,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colors.primary.copy(alpha = 0.87f),
        modifier = modifier,
    )
}
