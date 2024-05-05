package com.uragiristereo.mikansei.feature.image.more.tags

import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Tag
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.getCategoryColor
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun MoreTagItem(
    tag: Tag,
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val clipboardManager = LocalClipboardManager.current
    val hapticFeedback = LocalHapticFeedback.current
    val context = LocalContext.current

    val isLight = MaterialTheme.colors.isLight

    val tagColor = remember(tag, isLight) {
        tag.category.getCategoryColor(isLight)
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
            .width(IntrinsicSize.Max)
            .clip(RoundedCornerShape(size = 8.dp))
            .background(backgroundColor)
            .combinedClickable(
                onClick = {
                    onSelectedChange(!selected)
                },
                onLongClick = {
                    clipboardManager.setText(AnnotatedString(tag.name))
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.TIRAMISU) {
                        Toast
                            .makeText(context, "Tag copied to the clipboard!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
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
