package com.uragiristereo.mejiboard.feature.home.posts.grid

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PostLabel(
    fileType: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = fileType.uppercase(),
        color = MaterialTheme.colors.primary,
        style = MaterialTheme.typography.overline,
        fontWeight = FontWeight.ExtraBold,
        modifier = modifier
            .padding(
                start = 8.dp,
                bottom = 8.dp,
            )
            .clip(RoundedCornerShape(size = 4.dp))
            .background(MaterialTheme.colors.background.copy(alpha = 0.7f))
            .padding(all = 4.dp),
    )
}
