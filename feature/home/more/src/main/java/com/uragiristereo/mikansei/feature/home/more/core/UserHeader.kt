package com.uragiristereo.mikansei.feature.home.more.core

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Profile
import com.uragiristereo.mikansei.core.product.theme.MikanseiTheme
import com.uragiristereo.mikansei.core.resources.R

@Composable
internal fun UserHeader(
    name: String,
    nameAlias: String,
    userId: Int,
    level: Profile.Level,
    isOnlyAnonUserExist: Boolean,
    onProfileClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onMoreClick: () -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.12f),
                shape = RoundedCornerShape(8.dp),
            )
            .clip(RoundedCornerShape(8.dp))
//            .clickable(onClick = onProfileClick)
            .padding(all = 16.dp),
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
                text = nameAlias,
                style = MaterialTheme.typography.subtitle1,
                fontSize = 20.sp,
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                style = MaterialTheme.typography.body2,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = userId.toString(),
                style = MaterialTheme.typography.body2,
                fontSize = 14.sp,
            )

            Text(
                text = level.name,
                style = MaterialTheme.typography.body2,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = when {
                    MaterialTheme.colors.isLight -> level.lightColor
                    else -> level.darkColor
                },
            )
        }

        Row {
            IconButton(
                onClick = onSettingsClick,
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.settings_account_box),
                        contentDescription = null,
                        tint = MaterialTheme.colors.onBackground.copy(alpha = ContentAlpha.medium),
                    )
                },
            )

            IconButton(
                onClick = when {
                    isOnlyAnonUserExist -> onLoginClick
                    else -> onMoreClick
                },
                content = {
                    Icon(
                        painter = painterResource(
                            id = when {
                                isOnlyAnonUserExist -> R.drawable.login
                                else -> R.drawable.expand_more
                            }
                        ),
                        contentDescription = null,
                        tint = MaterialTheme.colors.onBackground.copy(alpha = ContentAlpha.medium),
                    )
                },
            )
        }
    }
}

@Preview
@Composable
private fun UserHeaderPreview() {
    MikanseiTheme {
        Surface {
            UserHeader(
                name = "proximity",
                nameAlias = "PR",
                userId = 101001,
                level = Profile.Level.Member,
                isOnlyAnonUserExist = true,
                onProfileClick = { },
                onSettingsClick = { },
                onMoreClick = { },
                onLoginClick = { },
            )
        }
    }
}
