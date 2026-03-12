package com.uragiristereo.mikansei.feature.home.posts.more.core

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.composable.AnimatedTextCounter

@Composable
internal fun FavoriteSection(
    checked: Boolean?,
    onCheckedChange: (Boolean) -> Unit,
    count: Int?,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    val checkedFalseFallback = checked ?: false
    val contentColor = when {
        checkedFalseFallback && enabled -> MaterialTheme.colors.primary.copy(alpha = ContentAlpha.high)
        enabled -> MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.high)
        else -> MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    onCheckedChange(!checkedFalseFallback)
                },
                enabled = enabled,
            )
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp,
            ),
    ) {
        Icon(
            painter = painterResource(
                id = when {
                    checkedFalseFallback -> R.drawable.favorite_fill
                    else -> R.drawable.favorite
                },
            ),
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier
                .padding(end = 32.dp)
                .size(24.dp),
        )

        Row {
            AnimatedContent(
                targetState = checked,
                transitionSpec = {
                    EnterTransition.None togetherWith ExitTransition.None using SizeTransform { _, _ ->
                        if (initialState != null) {
                            spring(
                                stiffness = Spring.StiffnessMediumLow,
                                visibilityThreshold = IntSize.VisibilityThreshold
                            )
                        } else {
                            snap()
                        }
                    }
                },
            ) { checked ->
                Text(
                    text = when {
                        checked == true -> "Favorited"
                        else -> "Favorite"
                    },
                    style = MaterialTheme.typography.body1,
                    color = contentColor,
                )
            }

            Text(
                text = " (",
                style = MaterialTheme.typography.body1,
                color = contentColor,
            )

            AnimatedTextCounter(
                count = count,
                style = MaterialTheme.typography.body1,
                color = contentColor,
            )

            Text(
                text = ")",
                style = MaterialTheme.typography.body1,
                color = contentColor,
            )
        }
    }
}
