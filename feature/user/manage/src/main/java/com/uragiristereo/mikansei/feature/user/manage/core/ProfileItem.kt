package com.uragiristereo.mikansei.feature.user.manage.core

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
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
internal fun ProfileItem(
    user: Profile,
    onSettingsClick: () -> Unit,
    onActivateClick: (Profile) -> Unit,
    onLogoutClick: (Profile) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isDropDownExpanded by rememberSaveable { mutableStateOf(false) }

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
                        color = MaterialTheme.colors.primary,
                    )
                }

                when {
                    user.mikansei.isActive -> IconButton(
                        onClick = onSettingsClick,
                        content = {
                            Icon(
                                painter = painterResource(id = R.drawable.settings),
                                contentDescription = null,
                                tint = MaterialTheme.colors.onSurface.copy(alpha = 0.74f),
                            )
                        },
                    )
                    else -> Box {
                        IconButton(
                            onClick = {
                                isDropDownExpanded = true
                            },
                            content = {
                                Icon(
                                    painter = painterResource(id = R.drawable.more_vert),
                                    contentDescription = null,
                                    tint = MaterialTheme.colors.onSurface.copy(alpha = 0.74f),
                                )
                            },
                        )

                        ProfileDropDownMenu(
                            isExpanded = isDropDownExpanded,
                            showLogout = user.id != 0,
                            onSetActiveClick = {
                                isDropDownExpanded = false
                                onActivateClick(user)
                            },
                            onLogoutClick = {
                                isDropDownExpanded = false
                                onLogoutClick(user)
                            },
                            onDismiss = {
                                isDropDownExpanded = false
                            },
                        )
                    }
                }
            }

            Divider()
        }
    }
}
