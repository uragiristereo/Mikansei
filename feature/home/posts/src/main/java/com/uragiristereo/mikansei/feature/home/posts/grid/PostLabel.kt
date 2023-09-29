package com.uragiristereo.mikansei.feature.home.posts.grid

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.resources.R

@Composable
internal fun PostLabel(
    fileType: String,
    hasSound: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(
                start = 8.dp,
                bottom = 8.dp,
            )
            .clip(RoundedCornerShape(size = 4.dp))
            .background(MaterialTheme.colors.background.copy(alpha = 0.7f))
            .padding(all = 4.dp),
    ) {
        Text(
            text = fileType.uppercase(),
            color = MaterialTheme.colors.primary,
            style = MaterialTheme.typography.overline,
            fontWeight = FontWeight.ExtraBold,
        )

        if (hasSound) {
            Icon(
                painter = painterResource(id = R.drawable.volume_up),
                contentDescription = null,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(16.dp),
            )
        } else {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
