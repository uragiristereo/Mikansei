package com.uragiristereo.mikansei.feature.filters.column

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.composable.SettingTip

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun FiltersColumn(
    columnState: LazyListState,
    items: List<FilterItem>,
    onItemSelected: (FilterItem) -> Unit,
    selectionMode: Boolean,
    enabled: Boolean,
    onOutsideTapped: () -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    Column {
        LazyColumn(
            state = columnState,
            contentPadding = contentPadding,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxWidth(),
        ) {
            item {
                SettingTip(text = stringResource(id = R.string.filters_tip))
            }

            item {
                Divider()
            }

            itemsIndexed(
                items = items,
                key = { _, item -> item.tags },
            ) { index, item ->
                FiltersColumnItem(
                    text = item.tags,
                    isEven = index % 2 == 0,
                    selected = item.selected,
                    enabled = enabled,
                    selectionMode = selectionMode,
                    onSelectedClick = {
                        onItemSelected(item.copy(selected = it))
                    },
                    onLongClick = {
                        onItemSelected(item.copy(selected = true))
                    },
                    modifier = Modifier.animateItemPlacement(),
                )
            }

            if (items.isNotEmpty()) {
                item {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp + 32.dp)
                            .pointerInput(key1 = Unit) {
                                detectTapGestures {
                                    onOutsideTapped()
                                }
                            }
                    )
                }
            }
        }

        Box(
            modifier = Modifier.weight(1f),
        ) {
            if (items.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.filters_no_tags).uppercase(),
                    style = MaterialTheme.typography.overline,
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .navigationBarsPadding()
                        .padding(bottom = 56.dp + 32.dp)
                        .align(Alignment.Center),
                )
            } else {
                Spacer(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(key1 = Unit) {
                            detectTapGestures {
                                onOutsideTapped()
                            }
                        }
                )
            }
        }
    }
}
