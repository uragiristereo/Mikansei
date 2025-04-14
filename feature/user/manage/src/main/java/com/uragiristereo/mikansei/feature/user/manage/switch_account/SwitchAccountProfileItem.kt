package com.uragiristereo.mikansei.feature.user.manage.switch_account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Profile
import com.uragiristereo.mikansei.core.resources.R

@Composable
internal fun SwitchAccountProfileItem(
    user: Profile,
    onActivateClick: (Profile) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .let {
                when {
                    user.mikansei.isActive -> it
                    else -> it.clickable {
                        onActivateClick(user)
                    }
                }
            }
            .padding(start = 16.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(
                    MaterialTheme.colors.onBackground
                        .copy(alpha = 0.2f)
                        .compositeOver(MaterialTheme.colors.background)
                ),
        ) {
            Text(
                text = user.mikansei.nameAlias,
                style = MaterialTheme.typography.subtitle1,
                fontSize = 20.sp,
            )
        }

        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .padding(end = 16.dp),
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.body2,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )

                    Text(
                        text = user.id.toString(),
                        style = MaterialTheme.typography.body2,
                        fontSize = 14.sp,
                    )

                    Text(
                        text = user.level.name,
                        style = MaterialTheme.typography.body2,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = when {
                            MaterialTheme.colors.isLight -> user.level.lightColor
                            else -> user.level.darkColor
                        },
                    )
                }

                if (user.mikansei.isActive) {
                    Icon(
                        painter = painterResource(id = R.drawable.done),
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary,
                    )
                }
            }

            Divider()
        }
    }
}
