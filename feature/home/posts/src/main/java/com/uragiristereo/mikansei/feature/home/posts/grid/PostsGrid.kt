package com.uragiristereo.mikansei.feature.home.posts.grid

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.model.Constants
import com.uragiristereo.mikansei.core.model.danbooru.post.Post
import com.uragiristereo.mikansei.core.ui.WindowSize
import com.uragiristereo.mikansei.core.ui.rememberWindowSize

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun PostsGrid(
    posts: List<Post>,
    gridState: LazyStaggeredGridState,
    canLoadMore: Boolean,
    topAppBarHeight: Dp,
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
        contentPadding = PaddingValues(
            start = 8.dp,
            end = 8.dp,
            top = 8.dp + topAppBarHeight,
            bottom = 8.dp + 1.dp +
                    WindowInsets.navigationBars
                        .asPaddingValues()
                        .calculateBottomPadding() +
                    when (windowSize) {
                        WindowSize.COMPACT -> 56.dp
                        else -> 0.dp
                    },
        ),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
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
            item(key = Constants.KEY_LOAD_MORE_PROGRESS) {
                Spacer(modifier = Modifier.size(1.dp))
            }
        }
    }
}
