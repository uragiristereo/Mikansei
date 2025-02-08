package com.uragiristereo.mikansei.feature.image.core

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.uragiristereo.mikansei.core.resources.R

@Composable
internal fun DownloadShareRow(
    onDownloadClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = modifier,
    ) {
        IconButton(
            onClick = onDownloadClick,
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.download),
                    contentDescription = null,
                    tint = Color.White,
                )
            },
        )

        IconButton(
            onClick = onShareClick,
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.share),
                    contentDescription = null,
                    tint = Color.White,
                )
            },
        )
    }
}
