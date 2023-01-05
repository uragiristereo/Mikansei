package com.uragiristereo.mejiboard.feature.search.quick_shortcut_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.uragiristereo.mejiboard.R
import com.uragiristereo.mejiboard.core.common.ui.extension.backgroundElevation

@Composable
fun SearchQuickShortcutBar(
    query: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxWidth(),
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .background(MaterialTheme.colors.background.backgroundElevation()),
        ) {
            item {
                SearchQuickShortcutItem(
                    text = stringResource(id = R.string.clear_action).uppercase(),
                    onClick = {
                        onQueryChange(TextFieldValue())
                    },
                )
            }

            item {
                SearchQuickShortcutItem(
                    text = stringResource(id = R.string.search_space).uppercase(),
                    onClick = {
                        onQueryChange(
                            TextFieldValue(
                                text = query.text.replaceRange(
                                    startIndex = query.selection.start,
                                    endIndex = query.selection.start,
                                    replacement = " ",
                                ),
                                selection = TextRange(index = query.selection.start + 1),
                            )
                        )
                    },
                )
            }

            item {
                SearchQuickShortcutItem(
                    text = "_",
                    onClick = {
                        onQueryChange(
                            TextFieldValue(
                                text = query.text.replaceRange(
                                    startIndex = query.selection.start,
                                    endIndex = query.selection.start,
                                    replacement = "_",
                                ),
                                selection = TextRange(index = query.selection.start + 1),
                            )
                        )
                    },
                )
            }

            item {
                SearchQuickShortcutItem(
                    text = "-",
                    onClick = {
                        onQueryChange(
                            TextFieldValue(
                                text = query.text.replaceRange(
                                    startIndex = query.selection.start,
                                    endIndex = query.selection.start,
                                    replacement = "-",
                                ),
                                selection = TextRange(index = query.selection.start + 1),
                            )
                        )
                    },
                )
            }

            item {
                SearchQuickShortcutItem(
                    text = ":",
                    onClick = {
                        onQueryChange(
                            TextFieldValue(
                                text = query.text.replaceRange(
                                    startIndex = query.selection.start,
                                    endIndex = query.selection.start,
                                    replacement = ":",
                                ),
                                selection = TextRange(index = query.selection.start + 1),
                            )
                        )
                    },
                )
            }

            item {
                SearchQuickShortcutItem(
                    text = "(",
                    onClick = {
                        onQueryChange(
                            TextFieldValue(
                                text = query.text.replaceRange(
                                    startIndex = query.selection.start,
                                    endIndex = query.selection.start,
                                    replacement = "(",
                                ),
                                selection = TextRange(index = query.selection.start + 1),
                            )
                        )
                    },
                )
            }

            item {
                SearchQuickShortcutItem(
                    text = ")",
                    onClick = {
                        onQueryChange(
                            TextFieldValue(
                                text = query.text.replaceRange(
                                    startIndex = query.selection.start,
                                    endIndex = query.selection.start,
                                    replacement = ")",
                                ),
                                selection = TextRange(index = query.selection.start + 1),
                            )
                        )
                    },
                )
            }

            item {
                SearchQuickShortcutItem(
                    text = "{",
                    onClick = {
                        var replacement = "{"

                        if (query.selection.start > 0)
                            replacement = when {
                                query.text[query.selection.start - 1] == ' ' -> "{"
                                else -> " {"
                            }

                        onQueryChange(
                            TextFieldValue(
                                text = query.text.replaceRange(
                                    startIndex = query.selection.start,
                                    endIndex = query.selection.start,
                                    replacement = replacement,
                                ),
                                selection = TextRange(index = query.selection.start + replacement.length),
                            )
                        )
                    },
                )
            }

            item {
                SearchQuickShortcutItem(
                    text = "}",
                    onClick = {
                        var queryStartIndex = query.selection.start
                        var replacement = "} "

                        if (queryStartIndex < query.text.length)
                            replacement = when {
                                query.text[queryStartIndex] == ' ' -> "}"
                                else -> "} "
                            }

                        if (queryStartIndex > 0)
                            if (query.text[queryStartIndex - 1] == ' ')
                                queryStartIndex -= 1

                        onQueryChange(
                            TextFieldValue(
                                text = query.text.replaceRange(
                                    startIndex = queryStartIndex,
                                    endIndex = query.selection.end,
                                    replacement = replacement,
                                ),
                                selection = TextRange(index = queryStartIndex + replacement.length)
                            )
                        )
                    },
                )
            }

            item {
                SearchQuickShortcutItem(
                    text = "~",
                    onClick = {
                        var replacement = "~ "

                        if (query.selection.start < query.text.length)
                            replacement = when {
                                query.text[query.selection.start] == ' ' -> "~"
                                else -> "~ "
                            }

                        if (query.selection.start > 0)
                            if (query.text[query.selection.start - 1] != ' ')
                                replacement = " $replacement"

                        onQueryChange(
                            TextFieldValue(
                                text = query.text.replaceRange(
                                    startIndex = query.selection.start,
                                    endIndex = query.selection.start,
                                    replacement = replacement,
                                ),
                                selection = TextRange(index = query.selection.start + replacement.length)
                            )
                        )
                    },
                )
            }

            item {
                SearchQuickShortcutItem(
                    text = "*",
                    onClick = {
                        onQueryChange(
                            TextFieldValue(
                                text = query.text.replaceRange(
                                    startIndex = query.selection.start,
                                    endIndex = query.selection.start,
                                    replacement = "*",
                                ),
                                selection = TextRange(index = query.selection.start + 1)
                            )
                        )
                    },
                )
            }
        }
    }
}
