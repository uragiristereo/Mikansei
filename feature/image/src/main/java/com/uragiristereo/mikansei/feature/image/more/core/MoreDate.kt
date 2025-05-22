package com.uragiristereo.mikansei.feature.image.more.core

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun MoreDate(
    date: OffsetDateTime,
    modifier: Modifier = Modifier,
) {
    val formatted = remember(date) {
        val zonedDateTime = date.atZoneSameInstant(ZoneId.systemDefault())

        DateTimeFormatter
            .ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.MEDIUM)
            .format(zonedDateTime)
    }

    Text(
        text = formatted,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        modifier = modifier
            .padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 2.dp,
            ),
    )
}
