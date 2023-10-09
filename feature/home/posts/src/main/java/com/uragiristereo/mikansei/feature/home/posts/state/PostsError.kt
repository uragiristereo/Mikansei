package com.uragiristereo.mikansei.feature.home.posts.state

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.product.theme.MikanseiTheme
import com.uragiristereo.mikansei.core.resources.R

@Composable
internal fun PostsError(
    message: String,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
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
