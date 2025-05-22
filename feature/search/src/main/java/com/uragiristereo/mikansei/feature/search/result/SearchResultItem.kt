package com.uragiristereo.mikansei.feature.search.result

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Tag
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.getCategoryColor
import com.uragiristereo.mikansei.core.resources.R

@Composable
internal fun SearchResultItem(
    tag: Tag,
    delimiter: String,
    boldWord: String,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isLight = MaterialTheme.colors.isLight

    val tagColor = remember(tag, isLight) {
        tag.category.getCategoryColor(isLight)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .widthIn(max = 540.dp)
                .padding(
                    vertical = 12.dp,
                    horizontal = 16.dp,
                ),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.circle_fill),
                contentDescription = null,
                tint = tagColor,
                modifier = Modifier
                    .padding(end = 24.dp)
                    .size(12.dp),
            )

            Text(
                text = buildAnnotatedString {
                    val hasAntecedent = tag.antecedent != null
                    val tagToBold = tag.antecedent ?: tag.name
                    val newTag = "$delimiter${tagToBold}".lowercase()
                    val boldStartIndex = newTag.indexOf(string = boldWord)
                    val boldEndIndex = boldStartIndex + boldWord.length
                    val shouldBold = boldStartIndex != -1

                    if (shouldBold) {
                        withStyle(
                            style = SpanStyle(
                                fontStyle = when {
                                    hasAntecedent -> FontStyle.Italic
                                    else -> FontStyle.Normal
                                },
                            )
                        ) {
                            append(
                                text = newTag.substring(
                                    startIndex = 0,
                                    endIndex = boldStartIndex,
                                ),
                            )

                            withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                                append(text = newTag.substring(boldStartIndex, boldEndIndex))
                            }

                            append(text = newTag.substring(startIndex = boldEndIndex))
                        }
                    } else {
                        append(text = newTag)
                    }

                    if (hasAntecedent) {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                            append(text = "  ›  ")
                        }

                        append(text = tag.name)
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
            )

            Text(
                text = tag.postCountFormatted,
                textAlign = TextAlign.End,
                color = MaterialTheme.colors.onBackground.copy(alpha = ContentAlpha.medium),
                modifier = Modifier
                    .widthIn(min = 48.dp)
                    .padding(end = 16.dp),
            )

            Icon(
                painter = painterResource(id = R.drawable.north_west),
                contentDescription = null,
            )
        }
    }
}
