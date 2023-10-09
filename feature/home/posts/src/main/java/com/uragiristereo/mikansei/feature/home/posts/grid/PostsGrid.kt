package com.uragiristereo.mikansei.feature.home.posts.grid

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.model.Constants
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.ui.WindowSize
import com.uragiristereo.mikansei.core.ui.extension.plus
import com.uragiristereo.mikansei.core.ui.rememberWindowSize

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun PostsGrid(
    posts: List<Post>,
    gridState: LazyStaggeredGridState,
    canLoadMore: Boolean,
    contentPadding: PaddingValues,
    onItemClick: (Post) -> Unit,
    onItemLongPress: (Post) -> Unit,
    modifier: Modifier = Modifier,
) {
    val windowSize = rememberWindowSize()

    LazyVerticalStaggeredGrid(
        state = gridState,
        columns = StaggeredGridCells.Fixed(
            count = when (windowSize) {
                WindowSize.COMPACT -> 2
                else -> 4
            }
        ),
        contentPadding = contentPadding + PaddingValues(all = 8.dp),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
        modifier = modifier.fillMaxSize(),
    ) {
        items(
            items = posts,
            key = { it.id },
        ) { item ->
            PostItem(
                post = item,
                onClick = {
                    onItemClick(item)
                },
                onLongPress = {
                    onItemLongPress(item)
                },
            )
        }

        if (canLoadMore && posts.isNotEmpty()) {
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
