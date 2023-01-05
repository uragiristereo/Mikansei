package com.uragiristereo.mejiboard.feature.image.more.info

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uragiristereo.mejiboard.R

@Composable
fun MoreInfoButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(size = 4.dp))
                .clickable(
                    onClick = onClick,
                )
                .padding(
                    vertical = 4.dp,
                    horizontal = 8.dp,
                ),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.expand_more),
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp),
            )

            Text(
                text = stringResource(id = R.string.more_label),
                fontWeight = FontWeight.Medium,
            )
        }
    }
}
