package com.uragiristereo.mikansei.feature.wiki.posts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uragiristereo.mikansei.core.model.danbooru.Post

@Composable
fun PostsSection(
    posts: List<Post>,
    postCount: Long,
    onPostClick: (Post) -> Unit,
    onPostLabelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 4.dp,
                ),
        )

        Text(
            text = "Posts (${postCount})",
            style = MaterialTheme.typography.subtitle1,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.primary.copy(alpha = 0.87f),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .clickable(onClick = onPostLabelClick)
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp,
                ),
        )

        PostsRow(
            posts = posts,
            postCount = postCount,
            onPostClick = onPostClick,
            onShowMoreClick = onPostLabelClick,
        )
    }
}
