package com.uragiristereo.mejiboard.feature.home.posts.core

import android.content.res.Configuration
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun HomeSnackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    val configuration = LocalConfiguration.current

    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { data ->
            Snackbar(
                modifier = Modifier
                    .padding(all = 12.dp)
                    .windowInsetsPadding(
                        insets = WindowInsets.navigationBars.only(sides = WindowInsetsSides.Bottom),
                    )
                    .padding(
                        bottom = when (configuration.orientation) {
                            Configuration.ORIENTATION_LANDSCAPE -> 0.dp
                            else -> 56.dp
                        },
                    ),
                content = {
                    Text(text = data.message)
                },
            )
        },
        modifier = modifier
            .widthIn(max = 620.dp)
            .wrapContentHeight(Alignment.Bottom),
    )
}
