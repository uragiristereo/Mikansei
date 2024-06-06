package com.uragiristereo.mikansei.feature.user.deactivation.in_web

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.product.theme.MikanseiTheme
import com.uragiristereo.mikansei.core.product.theme.Theme

@Composable
fun UserDeactivationInWebScreen(
    innerPadding: PaddingValues,
    onOpenWebClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .verticalScroll(rememberScrollState())
            .padding(innerPadding)
            .padding(all = 16.dp),
    ) {
        Text(
            text = "Press the button below to open the Danbooru site in your default browser, after you have logged in there you will be redirected to the deactivation page.",
            style = MaterialTheme.typography.body2,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Button(
                onClick = onOpenWebClick,
                shape = RoundedCornerShape(percent = 50),
                modifier = Modifier.widthIn(min = 96.dp),
                content = {
                    Text(text = "Open in Browser")
                },
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "After the deactivation process is successful, press the button below to logout.",
            style = MaterialTheme.typography.body2,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Button(
                onClick = onLogoutClick,
                shape = RoundedCornerShape(percent = 50),
                modifier = Modifier.widthIn(min = 96.dp),
                content = {
                    Text(text = "Logout")
                },
            )
        }
    }
}

@Preview
@Composable
private fun UserDeactivationInWebScreenPreview() {
    MikanseiTheme(theme = Theme.DARK) {
        Surface {
            UserDeactivationInWebScreen(
                innerPadding = PaddingValues(),
                onOpenWebClick = {},
                onLogoutClick = {},
            )
        }
    }
}
