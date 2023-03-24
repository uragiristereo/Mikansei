package com.uragiristereo.mikansei.feature.search.core

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.resources.R

@Composable
internal fun SearchBrowseButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .widthIn(max = 540.dp)
                .padding(
                    top = 12.dp,
                    bottom = 12.dp,
                    start = 16.dp,
                    end = 16.dp,
                ),
        ) {
            Text(
                text = buildAnnotatedString {
                    append(text = "${stringResource(id = R.string.browse)} ")

                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(text = text.ifEmpty { stringResource(id = R.string.all_posts) })
                    }
                },
                modifier = Modifier
                    .weight(weight = 1f, fill = true)
                    .padding(end = 16.dp),
            )

            Icon(
                painter = painterResource(id = R.drawable.search),
                contentDescription = stringResource(id = R.string.search_label),
            )
        }
    }
}
