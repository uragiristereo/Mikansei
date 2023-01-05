package com.uragiristereo.mejiboard.feature.search.result

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.uragiristereo.mejiboard.R
import com.uragiristereo.mejiboard.domain.entity.booru.tag.Tag

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchResultItem(
    tag: Tag,
    delimiter: String,
    boldWord: String,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
                painter = painterResource(id = R.drawable.search),
                contentDescription = null,
                modifier = Modifier.padding(end = 16.dp),
            )

            Text(
                text = buildAnnotatedString {
                    val newTag = "$delimiter${tag.name}".lowercase()

                    val boldStartIndex = newTag.indexOf(string = boldWord)
                    val boldEndIndex = boldStartIndex + boldWord.length
                    val shouldBold = boldStartIndex != -1

                    if (shouldBold) {
                        append(
                            text = newTag.substring(
                                startIndex = 0,
                                endIndex = boldStartIndex,
                            ),
                        )

                        withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                            append(
                                text = newTag.substring(boldStartIndex, boldEndIndex),
                            )
                        }

                        append(
                            text = newTag.substring(startIndex = boldEndIndex),
                        )
                    } else {
                        append(text = newTag)
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
            )

            Text(
                text = tag.countFmt,
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
