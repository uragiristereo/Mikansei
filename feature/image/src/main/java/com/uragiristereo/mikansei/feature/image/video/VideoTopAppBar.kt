package com.uragiristereo.mikansei.feature.image.video

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.feature.image.core.ViewerTopAppBar

@Composable
internal fun VideoTopAppBar(
    postId: Int,
    onNavigateBack: () -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ViewerTopAppBar(
        postId = postId,
        onNavigateBack = onNavigateBack,
        actions = {
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.displayCutoutPadding(),
            ) {
                IconButton(
                    onClick = onMoreClick,
                    content = {
                        Icon(
                            painter = painterResource(id = R.drawable.more_vert),
                            contentDescription = null,
                            tint = Color.White,
                        )
                    },
                )
            }
        },
        modifier = modifier,
    )
}
