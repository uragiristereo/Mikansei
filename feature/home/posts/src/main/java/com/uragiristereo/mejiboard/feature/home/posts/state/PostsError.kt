package com.uragiristereo.mejiboard.feature.home.posts.state

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uragiristereo.mejiboard.core.resources.R

@Composable
fun PostsError(
    message: String,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(
                bottom = when {
                    isLandscape -> 0.dp
                    else -> 56.dp + 1.dp
                },
                start = when {
                    isLandscape -> 56.dp + 1.dp
                    else -> 0.dp
                },
            ),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.error),
                contentDescription = null,
                tint = MaterialTheme.colors.error,
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .size(48.dp)
                    .clip(CircleShape),
            )

            Text(
                text = "Error",
                style = MaterialTheme.typography.subtitle1,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.error,
                modifier = Modifier.padding(bottom = 8.dp),
            )

            Text(
                text = message,
                style = MaterialTheme.typography.subtitle2,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 32.dp),
            )

            Button(
                onClick = onRetryClick,
                shape = RoundedCornerShape(percent = 50),
                modifier = Modifier.widthIn(min = 96.dp),
                content = {
                    Text(text = "Retry")
                },
            )
        }
    }
}

@Preview
@Composable
private fun PostsErrorPreview() {
    com.uragiristereo.mejiboard.core.product.theme.MejiboardTheme {
        Surface {
            PostsError(
                message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut",
                onRetryClick = { },
            )
        }
    }
}
