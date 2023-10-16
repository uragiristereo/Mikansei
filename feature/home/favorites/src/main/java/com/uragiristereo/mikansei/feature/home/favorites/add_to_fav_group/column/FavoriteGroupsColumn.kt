package com.uragiristereo.mikansei.feature.home.favorites.add_to_fav_group.column

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.feature.home.favorites.add_to_fav_group.core.FavoriteGroup

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun FavoriteGroupsColumn(
    items: List<FavoriteGroup>,
    enabled: Boolean,
    onAddClick: (FavoriteGroup) -> Unit,
    onRemoveClick: (FavoriteGroup) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 8.dp),
        modifier = modifier,
    ) {
        items(
            items = items,
            key = { it.id },
        ) { item ->
            FavoriteGroupItem(
                item = item,
                enabled = enabled,
                onAddClick = onAddClick,
                onRemoveClick = onRemoveClick,
                modifier = Modifier.animateItemPlacement(),
            )
        }
    }
}
