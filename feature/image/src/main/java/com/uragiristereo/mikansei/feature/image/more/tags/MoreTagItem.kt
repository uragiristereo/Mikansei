package com.uragiristereo.mikansei.feature.image.more.tags

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Tag
import com.uragiristereo.mikansei.core.ui.extension.backgroundElevation

@Composable
internal fun MoreTagItem(
    tag: Tag,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MoreTagItem(
        tag = tag,
        selected = false,
        onSelectedChange = {
            onClick()
        },
        modifier = modifier,
    )
}

@Composable
internal fun MoreTagItem(
    tag: Tag,
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isLight = MaterialTheme.colors.isLight

    val tagColor = remember(tag, isLight) {
        when {
            isLight -> when (tag.category) {
                Tag.Category.GENERAL -> Color(0xFF0075F8)
                Tag.Category.CHARACTER -> Color(0xFF00AB2C)
                Tag.Category.COPYRIGHT -> Color(0xFFA800AA)
                Tag.Category.ARTIST -> Color(0xFFC00004)
                Tag.Category.META -> Color(0xFFFD9200)
                Tag.Category.UNKNOWN -> Color(0xFF0075F8)
            }

            else -> when (tag.category) {
                Tag.Category.GENERAL -> Color(0xFF009BE6)
                Tag.Category.CHARACTER -> Color(0xFF35C64A)
                Tag.Category.COPYRIGHT -> Color(0xFFC797FF)
                Tag.Category.ARTIST -> Color(0xFFFF8A8B)
                Tag.Category.META -> Color(0xFFEAD084)
                Tag.Category.UNKNOWN -> Color(0xFF009BE6)
            }
        }
    }

    val backgroundColor = when {
        selected -> tagColor
        else -> MaterialTheme.colors.background.backgroundElevation(3.dp)
    }
    val contentColor = when {
        !selected -> tagColor
        else -> Color.White.copy(alpha = 0.7f)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .width(IntrinsicSize.Min)
            .clip(RoundedCornerShape(size = 8.dp))
            .background(backgroundColor)
            .clickable(
                onClick = {
                    onSelectedChange(!selected)
                },
            )
            .padding(
                vertical = 6.dp,
                horizontal = 10.dp,
            ),
    ) {
        Text(
            text = tag.name,
            fontWeight = FontWeight.Medium,
            color = contentColor,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
        )

        Text(
            text = tag.postCountFormatted,
            color = contentColor,
            fontWeight = FontWeight.Light,
        )
    }
}
