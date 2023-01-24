package com.uragiristereo.mejiboard.feature.home.posts.grid

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.uragiristereo.mejiboard.core.data.Constants
import com.uragiristereo.mejiboard.core.model.booru.post.Post
import com.uragiristereo.mejiboard.core.ui.WindowSize
import com.uragiristereo.mejiboard.core.ui.rememberWindowSize

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostsGrid(
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
