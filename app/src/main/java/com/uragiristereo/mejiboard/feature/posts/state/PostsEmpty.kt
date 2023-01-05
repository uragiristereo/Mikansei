package com.uragiristereo.mejiboard.feature.posts.state

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.uragiristereo.mejiboard.core.resources.R
import com.uragiristereo.mejiboard.core.common.ui.composable.Banner

@Composable
fun PostsEmpty(
    modifier: Modifier = Modifier,
) {
    Banner(
        icon = painterResource(id = R.drawable.image_not_supported),
        text = {
            Text(
                text = "Result is empty, please check your tags and filters",
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        },
        modifier = modifier,
    )
}
