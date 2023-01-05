package com.uragiristereo.mejiboard.core.common.ui.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OverlineTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.overline,
        fontSize = 12.sp,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = 8.dp,
                bottom = 4.dp,
                start = 8.dp,
                end = 8.dp,
            ),
    )
}
