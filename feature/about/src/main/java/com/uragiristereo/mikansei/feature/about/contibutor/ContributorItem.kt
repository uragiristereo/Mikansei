package com.uragiristereo.mikansei.feature.about.contibutor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.uragiristereo.mikansei.core.product.theme.MikanseiTheme
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.extension.backgroundElevation
import com.uragiristereo.mikansei.feature.about.domain.entity.GitHubContributor

@Composable
fun ContributorItem(
    item: GitHubContributor,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val contributions: Any = when {
        item.contributions > 0 -> item.contributions
        else -> "-"
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp)
            .padding(
                start = 8.dp,
                end = 16.dp
            ),
    ) {
        AsyncImage(
            model = item.avatarUrl,
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.background.backgroundElevation()),
        )

        Spacer(modifier = Modifier.width(24.dp))

        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = item.login,
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Medium,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "$contributions contributions",
                style = MaterialTheme.typography.caption,
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Icon(
            painter = painterResource(id = R.drawable.github_icon),
            contentDescription = null,
            tint = LocalContentColor.current.copy(alpha = ContentAlpha.medium),
            modifier = Modifier.size(24.dp),
        )
    }
}

@Preview
@Composable
private fun ContributorItemPreview() {
    MikanseiTheme {
        Surface {
            ContributorItem(
                item = GitHubContributor(
                    login = "uragiristereo",
                    id = 52477630,
                    avatarUrl = "https://avatars.githubusercontent.com/u/52477630?v=4",
                    htmlUrl = "https://github.com/uragiristereo",
                    contributions = 454,
                ),
                onClick = {},
            )
        }
    }
}
