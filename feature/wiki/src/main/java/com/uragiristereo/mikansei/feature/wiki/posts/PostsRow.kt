package com.uragiristereo.mikansei.feature.wiki.posts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.ui.composable.SidesGradient2
import com.uragiristereo.mikansei.core.ui.extension.plus

@Composable
fun PostsRow(
    posts: List<Post>,
    postCount: Long,
    onPostClick: (Post) -> Unit,
    onShowMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SidesGradient2(modifier = modifier) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp) + PaddingValues(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(posts) { item ->
                PostItem(
                    post = item,
                    onClick = onPostClick,
                )
            }

            if (postCount > 10L) {
                item {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.height(160.dp),
                    ) {
                        TextButton(
                            onClick = onShowMoreClick,
                        ) {
                            Text(text = "Show More".uppercase())
                        }
                    }
                }
            }
        }
    }
}
