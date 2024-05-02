package com.uragiristereo.mikansei.feature.saved_searches.delete

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.SavedSearch
import com.uragiristereo.mikansei.core.product.theme.MikanseiTheme
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.composable.ClickableSection

@Composable
fun DeleteSavedSearchContent(
    savedSearch: SavedSearch,
    onDeleteClick: () -> Unit = {},
) {
    Column {
        Text(
            text = buildAnnotatedString {
                append(text = "Are you sure want to delete this saved search?\n\n")
                append(text = "Labels: ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(
                        text = when {
                            savedSearch.labels.isNotEmpty() -> {
                                savedSearch.labels.joinToString(separator = " ")
                            }

                            else -> "-"
                        }
                    )
                }
                append(text = "\n")
                append(text = "Query: ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(text = savedSearch.query)
                }
            },
            style = MaterialTheme.typography.caption,
            fontSize = 14.sp,
            color = MaterialTheme.colors.onBackground.copy(alpha = ContentAlpha.medium),
            modifier = Modifier
                .padding(
                    top = 24.dp,
                    bottom = 8.dp,
                    start = 16.dp,
                    end = 16.dp,
                ),
        )

        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colors.error) {
            ClickableSection(
                title = stringResource(id = R.string.delete_confirm),
                icon = painterResource(id = R.drawable.delete),
                onClick = onDeleteClick,
                modifier = Modifier
                    .windowInsetsPadding(
                        insets = WindowInsets.navigationBars.only(sides = WindowInsetsSides.Bottom),
                    ),
            )
        }
    }
}

@Preview
@Composable
private fun DeleteSavedSearchContentPreview() {
    MikanseiTheme {
        Surface {
            DeleteSavedSearchContent(
                savedSearch = SavedSearch(
                    id = 1,
                    query = "kagamine_rin serafuku",
                    labels = listOf(
                        "rinchan",
                        "rinn",
                    )
                )
            )
        }
    }
}
