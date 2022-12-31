package com.uragiristereo.mejiboard.presentation.common.composable.product

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.uragiristereo.mejiboard.presentation.common.extension.backgroundElevation

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProductDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val configuration = LocalConfiguration.current

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = configuration.orientation == Configuration.ORIENTATION_PORTRAIT,
        ),
        content = {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(size = 8.dp))
                    .background(MaterialTheme.colors.background.backgroundElevation()),
            ) {
                content()
            }
        },
    )
}