package com.uragiristereo.mejiboard.core.common.ui.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.uragiristereo.mejiboard.core.resources.R

@Composable
fun SettingTip(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.widthIn(max = 620.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(
                    top = 8.dp,
                    bottom = 8.dp,
                    start = 18.dp,
                    end = 16.dp,
                ),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.info),
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface
                    .copy(alpha = ContentAlpha.medium),
                modifier = Modifier
                    .padding(end = 38.dp)
                    .size(18.dp),
            )

            Text(
                text = text,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface
                    .copy(alpha = ContentAlpha.medium),
            )
        }
    }
}
