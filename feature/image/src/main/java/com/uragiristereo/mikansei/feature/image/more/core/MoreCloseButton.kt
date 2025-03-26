package com.uragiristereo.mikansei.feature.image.more.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.resources.R

@Composable
internal fun MoreCloseButton(
    visible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (visible) {
        IconButton(
            onClick = onClick,
            modifier = modifier
                .padding(horizontal = 8.dp)
                .padding(bottom = 8.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.background.copy(alpha = ContentAlpha.medium)),
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = null,
                )
            },
        )
    }
//    )
}
