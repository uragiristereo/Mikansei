package com.uragiristereo.mikansei.feature.user.deactivation.methods

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.product.theme.MikanseiTheme
import com.uragiristereo.mikansei.core.product.theme.Theme
import com.uragiristereo.mikansei.core.ui.composable.SectionTitle
import com.uragiristereo.mikansei.core.ui.extension.backgroundElevation

@Composable
fun UserDeactivationMethodsScreen(
    innerPadding: PaddingValues,
    onInAppClick: () -> Unit,
    onInWebClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colors.background)
            .padding(innerPadding)
            .padding(all = 16.dp),
    ) {
        SectionTitle(
            text = "Where do you want to delete your account?",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colors.background.backgroundElevation())
                .clickable(onClick = onInAppClick)
                .padding(all = 16.dp),
        ) {
            Text(
                text = "Deactivate in the app",
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Medium,
            )

            Text(
                text = "recommended",
                style = MaterialTheme.typography.caption,
                fontWeight = FontWeight.Medium,
                fontStyle = FontStyle.Italic,
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = buildAnnotatedString {
                    append(
                        "•\t\tYou only need to enter your password.\n" +
                                "•\t\tClick on the deactivate now button to send your request.\n" +
                                "•\t\tAfter the deactivation process is succeeded, you will be " +
                                "automatically logged out from the app."
                    )
                },
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium),
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colors.background.backgroundElevation())
                .clickable(onClick = onInWebClick)
                .padding(all = 16.dp),
        ) {
            Text(
                text = "Deactivate in the Danbooru site",
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Medium,
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = buildAnnotatedString {
                    append(
                        "•\t\tClick on the open in browser button to login the Danbooru site " +
                                "on your default browser.\n" +
                                "•\t\tYou need to enter your username and password in the Danbooru site.\n" +
                                "•\t\tAfter the deactivation process is succeeded, you need to " +
                                "manually log out in the app to avoid issues."
                    )
                },
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview
@Composable
private fun UserDeactivationMethodsScreenPreview() {
    MikanseiTheme(theme = Theme.DARK) {
        Surface {
            UserDeactivationMethodsScreen(
                innerPadding = PaddingValues(),
                onInAppClick = {},
                onInWebClick = {},
            )
        }
    }
}
