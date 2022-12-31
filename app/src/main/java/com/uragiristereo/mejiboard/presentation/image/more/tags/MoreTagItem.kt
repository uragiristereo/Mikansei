package com.uragiristereo.mejiboard.presentation.image.more.tags

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
import com.uragiristereo.mejiboard.domain.entity.source.tag.Tag
import com.uragiristereo.mejiboard.domain.entity.source.tag.TagType
import com.uragiristereo.mejiboard.presentation.common.extension.backgroundElevation

@Composable
fun MoreTagItem(
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
fun MoreTagItem(
    tag: Tag,
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tagColor = remember(tag) {
        when (tag.type) {
            TagType.GENERAL -> Color(0xFF47A4FD)
            TagType.CHARACTER -> Color(0xFF8052FF)
            TagType.COPYRIGHT -> Color(0xFF00B26F)
            TagType.ARTIST -> Color(0xFFFF6C6D)
            TagType.METADATA -> Color(0xFFFF9E53)
            TagType.NONE -> Color(0xFF47A4FD)
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
            .clip(RoundedCornerShape(size = 16.dp))
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
            fontWeight = FontWeight.Bold,
            color = contentColor,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
        )

        Text(
            text = tag.countFmt,
            color = contentColor,
        )
    }
}
