package com.uragiristereo.mejiboard.core.common.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.uragiristereo.mejiboard.R
import com.uragiristereo.mejiboard.core.product.theme.MejiboardTheme

@Composable
fun ClickableSection(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: AnnotatedString? = null,
    icon: Painter? = null,
    padStart: Boolean = true,
    verticalPadding: Dp = 12.dp,
    horizontalPadding: Dp = 16.dp,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
            )
            .padding(
                horizontal = horizontalPadding,
                vertical = verticalPadding,
            ),
    ) {
        when {
            icon != null -> {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 32.dp)
                        .size(24.dp),
                )
            }

            padStart ->
                Spacer(
                    modifier = Modifier
                        .padding(end = 32.dp)
                        .size(24.dp),
                )
        }

        Column(
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.body1,
            )

            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium)
                )
            }
        }
    }
}

@Composable
fun ClickableSection(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String,
    icon: Painter? = null,
    padStart: Boolean = true,
    verticalPadding: Dp = 12.dp,
    horizontalPadding: Dp = 16.dp,
) {
    ClickableSection(
        title = title,
        onClick = onClick,
        modifier = modifier,
        subtitle = AnnotatedString(text = subtitle),
        icon = icon,
        padStart = padStart,
        verticalPadding = verticalPadding,
        horizontalPadding = horizontalPadding,
    )
}

@Preview
@Composable
private fun ClickableSectionPreview() {
    MejiboardTheme {
        Surface {
            ClickableSection(
                title = "Selected booru",
                subtitle = "Gelbooru (gelbooru.com)",
                icon = painterResource(id = R.drawable.search),
                onClick = { },
            )
        }
    }
}
