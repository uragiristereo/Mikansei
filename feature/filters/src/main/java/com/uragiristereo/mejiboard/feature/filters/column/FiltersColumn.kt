package com.uragiristereo.mejiboard.feature.filters.column

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uragiristereo.mejiboard.core.resources.R
import com.uragiristereo.mejiboard.core.ui.composable.NavigationBarSpacer
import com.uragiristereo.mejiboard.core.ui.composable.SettingTip
import com.uragiristereo.mejiboard.core.ui.composable.SettingToggle
import com.uragiristereo.mejiboard.core.ui.database.FilterItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FiltersColumn(
    columnState: LazyListState,
    items: List<FilterItem>,
    onItemChange: (FilterItem) -> Unit,
    onItemSelected: (FilterItem) -> Unit,
    selectionMode: Boolean,
    toggleChecked: Boolean,
    onToggleChecked: (Boolean) -> Unit,
    onOutsideTapped: () -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(paddingValues = contentPadding),
    ) {
        LazyColumn(
            state = columnState,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                SettingToggle(
                    checked = toggleChecked,
                    onCheckedChange = onToggleChecked,
                )
            }

            item {
                SettingTip(
                    text = stringResource(id = R.string.filters_tip),
                )
            }

            item {
                Divider()
            }

            itemsIndexed(
                items = items,
                key = { _, item ->
                    item.id
                },
            ) { index, item ->
                FiltersColumnItem(
                    text = item.tag,
                    isEven = index % 2 == 0,
                    selected = item.selected,
                    selectionMode = selectionMode,
                    enabled = item.enabled,
                    onEnabledChange = {
                        onItemChange(item.copy(enabled = it))
                    },
                    onSelectedClick = {
                        onItemSelected(item.copy(selected = it))
                    },
                    onLongClick = {
                        onItemSelected(item.copy(selected = true))
                    },
                    modifier = Modifier
                        .animateItemPlacement(),
                )
            }

            if (items.isNotEmpty()) {
                item {
                    NavigationBarSpacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .pointerInput(key1 = Unit) {
                                detectTapGestures {
                                    onOutsideTapped()
                                }
                            }
                            .padding(
                                bottom = 56.dp + 32.dp,
                            ),
                    )
                }
            }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .pointerInput(key1 = Unit) {
                    detectTapGestures {
                        onOutsideTapped()
                    }
                }
                .let {
                    when {
                        items.isEmpty() -> it
                            .windowInsetsPadding(
                                insets = WindowInsets.navigationBars.only(sides = WindowInsetsSides.Bottom),
                            )

                        else -> it
                    }
                },
        ) {
            if (items.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.filters_no_tags).uppercase(),
                    style = MaterialTheme.typography.overline,
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.primary,
                )
            }
        }
    }
}
