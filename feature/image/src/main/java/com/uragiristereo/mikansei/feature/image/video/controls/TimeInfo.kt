package com.uragiristereo.mikansei.feature.image.video.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun TimeInfo(
    elapsed: String,
    total: String,
    modifier: Modifier = Modifier,
) {
    val textShadow = Shadow(
        color = Color.Black.copy(alpha = ContentAlpha.medium),
        blurRadius = 1f,
    )

    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = Color.White,
                    shadow = textShadow,
                ),
                block = {
                    append(text = elapsed)
                },
            )

            withStyle(
                style = SpanStyle(
                    color = Color.White.copy(alpha = 0.9f),
                    shadow = textShadow,
                ),
                block = {
                    append(text = " / $total")
                },
            )
        },
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        modifier = modifier
            .padding(horizontal = 14.dp)
            .clip(RoundedCornerShape(size = 2.dp))
            .background(Color.Black.copy(alpha = 0.2f))
            .padding(all = 2.dp),
    )
}
