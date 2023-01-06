package com.uragiristereo.mejiboard.feature.home.posts.grid

import android.content.res.Configuration
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.uragiristereo.mejiboard.core.common.data.Constants
import com.uragiristereo.mejiboard.core.model.booru.post.Post

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostGrid(
    posts: List<Post>,
    gridState: LazyStaggeredGridState,
    canLoadMore: Boolean,
    topAppBarHeight: Dp,
    onItemClick: (Post) -> Unit,
    onItemLongPress: (Post) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

        LazyVerticalStaggeredGrid(
            state = gridState,
            columns = StaggeredGridCells.Fixed(
                count = when {
                    isLandscape -> 4
                    else -> 2
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
                        when {
                            isLandscape -> 0.dp
                            else -> 56.dp
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
