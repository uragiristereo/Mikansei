package com.uragiristereo.mikansei.feature.search.core

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.composable.Chips
import com.uragiristereo.mikansei.core.ui.composable.SidesGradient

@Composable
internal fun SearchActionRow(
    historyEnabled: Boolean,
    onSelectedBooruClick: () -> Unit,
    onSavedSearchesClick: () -> Unit,
    onFiltersClick: () -> Unit,
    onHistoryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Box {
            LazyRow(
                contentPadding = PaddingValues(
                    vertical = 8.dp,
                    horizontal = 16.dp,
                ),
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(),
            ) {
                item {
                    Chips(
                        text = stringResource(id = R.string.saved_searches_label),
                        icon = painterResource(id = R.drawable.sell),
                        onClick = onSavedSearchesClick,
                        modifier = Modifier.padding(end = 8.dp),
                    )
                }

                item {
                    Chips(
                        text = stringResource(id = R.string.filters_label),
                        icon = painterResource(id = R.drawable.filter_list),
                        onSelectedChange = { onFiltersClick() },
                        modifier = Modifier.padding(end = 8.dp),
                    )
                }

                item {
                    Chips(
                        text = stringResource(id = R.string.search_history_short_label),
                        icon = painterResource(id = R.drawable.history),
                        selected = historyEnabled,
                        onSelectedChange = { onHistoryClick() },
                    )
                }
            }

            SidesGradient(
                color = MaterialTheme.colors.background,
                modifier = Modifier.matchParentSize(),
            )
        }

        Divider()
    }
}
