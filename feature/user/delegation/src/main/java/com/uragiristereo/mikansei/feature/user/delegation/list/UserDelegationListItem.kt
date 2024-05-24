package com.uragiristereo.mikansei.feature.user.delegation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.extension.backgroundElevation

@Composable
fun UserDelegationListItem(
    item: UserDelegation,
    onClick: (UserDelegation) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = when {
                    item.selected -> MaterialTheme.colors.background.backgroundElevation(4.dp)
                    else -> MaterialTheme.colors.background
                }
            )
            .clickable {
                onClick(item)
            }
            .padding(
                vertical = 16.dp,
                horizontal = 12.dp,
            ),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.done),
            contentDescription = null,
            tint = when {
                item.selected -> MaterialTheme.colors.primary
                else -> Color.Transparent
            },
        )

        Spacer(modifier = Modifier.width(32.dp))

        Text(
            text = item.name ?: "None",
            color = when {
                item.selected -> MaterialTheme.colors.primary
                else -> MaterialTheme.colors.onBackground
            },
            modifier = Modifier.weight(1f),
        )
    }
}
