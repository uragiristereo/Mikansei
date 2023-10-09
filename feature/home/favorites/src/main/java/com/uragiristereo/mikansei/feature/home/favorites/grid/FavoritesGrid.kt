package com.uragiristereo.mikansei.feature.home.favorites.grid

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Favorite
import com.uragiristereo.mikansei.core.ui.LocalWindowSizeHorizontal
import com.uragiristereo.mikansei.core.ui.WindowSize
import com.uragiristereo.mikansei.core.ui.extension.plus

@Composable
internal fun FavoritesGrid(
    items: List<Favorite>,
    contentPadding: PaddingValues,
    onFavoritesClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val windowSizeHorizontal = LocalWindowSizeHorizontal.current

    LazyVerticalGrid(
        columns = GridCells.Fixed(
            count = when (windowSizeHorizontal) {
                WindowSize.COMPACT -> 2
                WindowSize.MEDIUM -> 4
                WindowSize.EXPANDED -> 5
            },
        ),
        contentPadding = contentPadding + PaddingValues(all = 16.dp) + PaddingValues(bottom = 56.dp + 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxSize(),
    ) {
        items(items) { item ->
            FavoriteItem(
                item = item,
                onClick = {
                    onFavoritesClick(item.id)
                },
            )
        }
    }
}
