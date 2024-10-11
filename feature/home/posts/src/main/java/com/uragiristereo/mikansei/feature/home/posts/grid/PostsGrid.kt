package com.uragiristereo.mikansei.feature.home.posts.grid

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.model.Constants
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.ui.LocalWindowSizeHorizontal
import com.uragiristereo.mikansei.core.ui.WindowSize
import com.uragiristereo.mikansei.core.ui.entity.ImmutableList
import com.uragiristereo.mikansei.core.ui.extension.plus

@Composable
internal fun PostsGrid(
    posts: ImmutableList<Post>,
    gridState: LazyStaggeredGridState,
    canLoadMore: Boolean,
    contentPadding: PaddingValues,
    onItemClick: (Post) -> Unit,
    onItemLongPress: (Post) -> Unit,
    modifier: Modifier = Modifier,
) {
    val windowSizeHorizontal = LocalWindowSizeHorizontal.current
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val columnSize = when (windowSizeHorizontal) {
        WindowSize.COMPACT -> 2
        WindowSize.MEDIUM -> 4
        WindowSize.EXPANDED -> 5
    }

    val screenWidthPx = remember(configuration.screenWidthDp) {
        density.run {
            configuration.screenWidthDp.dp.toPx()
        }
    }

    val maxWidth = remember(screenWidthPx, columnSize) {
        (screenWidthPx / columnSize).toInt()
    }

    LazyVerticalStaggeredGrid(
        state = gridState,
        columns = StaggeredGridCells.Fixed(columnSize),
        contentPadding = contentPadding + PaddingValues(all = 8.dp),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
        modifier = modifier.fillMaxSize(),
    ) {
        items(
            items = posts.value,
            key = { it.id },
        ) { item ->
            PostItem(
                post = item,
                maxWidth = maxWidth,
                onClick = {
                    onItemClick(item)
                },
                onLongPress = {
                    onItemLongPress(item)
                },
                modifier = Modifier.animateItem(),
            )
        }

        if (canLoadMore && posts.value.isNotEmpty()) {
            item(
                key = Constants.KEY_LOAD_MORE_PROGRESS,
                span = StaggeredGridItemSpan.FullLine,
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
