package com.uragiristereo.mejiboard.feature.search.core

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.uragiristereo.mejiboard.core.model.booru.BooruSource
import com.uragiristereo.mejiboard.core.resources.R
import com.uragiristereo.mejiboard.core.ui.composable.Chips
import com.uragiristereo.mejiboard.core.ui.composable.SidesGradient

@Composable
fun SearchActionRow(
    selectedBooru: BooruSource,
    filtersEnabled: Boolean,
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
                        text = stringResource(id = selectedBooru.nameResId),
                        icon = painterResource(id = R.drawable.public_globe),
                        onClick = onSelectedBooruClick,
                        modifier = Modifier.padding(end = 8.dp),
                    )
                }

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
                        selected = filtersEnabled,
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
