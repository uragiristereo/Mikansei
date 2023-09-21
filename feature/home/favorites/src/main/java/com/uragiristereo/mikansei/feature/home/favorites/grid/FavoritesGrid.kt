package com.uragiristereo.mikansei.feature.home.favorites.grid

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.domain.entity.favorite.Favorite
import com.uragiristereo.mikansei.core.ui.WindowSize
import com.uragiristereo.mikansei.core.ui.extension.plus
import com.uragiristereo.mikansei.core.ui.rememberWindowSize

@Composable
internal fun FavoritesGrid(
    items: List<Favorite>,
    contentPadding: PaddingValues,
    onFavoritesClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val windowSize = rememberWindowSize()

    val gridSize = remember(windowSize) {
        when (windowSize) {
            WindowSize.COMPACT -> 2
            else -> 4
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(count = gridSize),
        contentPadding = contentPadding + PaddingValues(all = 16.dp),
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

        item(
            span = { GridItemSpan(currentLineSpan = gridSize) },
        ) {
            Spacer(
                modifier = Modifier
                    .navigationBarsPadding()
                    .height(
                        when (windowSize) {
                            WindowSize.COMPACT -> 57.dp + 48.dp
                            else -> 0.dp
                        }
                    ),
            )
        }
    }
}
