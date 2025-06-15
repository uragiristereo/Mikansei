package com.uragiristereo.mikansei.feature.saved_searches.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.SavedSearch
import com.uragiristereo.mikansei.core.ui.composable.SettingTip
import com.uragiristereo.mikansei.core.ui.entity.ImmutableList
import com.uragiristereo.mikansei.core.ui.extension.plus

@Composable
fun SavedSearchesList(
    isLoading: Boolean,
    items: ImmutableList<SavedSearch>,
    contentPadding: PaddingValues,
    expandedItemId: Int?,
    onExpandedItemIdChange: (Int?) -> Unit,
    actions: SavedSearchesListItemActions,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        contentPadding = contentPadding + PaddingValues(bottom = 56.dp + 32.dp),
        modifier = modifier.fillMaxSize(),
    ) {
        item {
            SettingTip(text = "You can browse your favorite tags quickly with saved searches.")
        }

        item {
            Divider()
        }

        if (isLoading || items.value.isNotEmpty()) {
            itemsIndexed(
                items = items.value,
                key = { _, item ->
                    item.id
                },
            ) { index, item ->
                SavedSearchesListItem(
                    item = item,
                    isEven = index % 2 == 0,
                    isExpanded = expandedItemId == item.id,
                    onToggleExpand = { expanded ->
                        if (expanded) {
                            onExpandedItemIdChange(item.id)
                        } else {
                            onExpandedItemIdChange(null)
                        }
                    },
                    actions = actions,
                    modifier = Modifier.animateItem(),
                )
            }
        } else {
            item {
                Text(
                    text = "You don't have any saved searches",
                    color = MaterialTheme.colors.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 32.dp,
                            horizontal = 16.dp,
                        ),
                )
            }
        }
    }
}
