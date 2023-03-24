package com.uragiristereo.mikansei.feature.home.posts.state

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.product.theme.MikanseiTheme
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.WindowSize
import com.uragiristereo.mikansei.core.ui.rememberWindowSize

@Composable
internal fun PostsError(
    message: String,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val windowSize = rememberWindowSize()

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(
                bottom = when (windowSize) {
                    WindowSize.COMPACT -> 56.dp + 1.dp
                    else -> 0.dp
                },
                start = when (windowSize) {
                    WindowSize.COMPACT -> 0.dp
                    else -> 56.dp + 1.dp
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
    MikanseiTheme {
        Surface {
            PostsError(
                message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut",
                onRetryClick = { },
            )
        }
    }
}
