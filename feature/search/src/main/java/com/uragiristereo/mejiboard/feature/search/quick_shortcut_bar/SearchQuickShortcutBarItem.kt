package com.uragiristereo.mejiboard.feature.search.quick_shortcut_bar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.uragiristereo.mejiboard.core.ui.extension.backgroundElevation

@Composable
internal fun SearchQuickShortcutItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.height(36.dp),
    ) {
        TextButton(
            shape = RectangleShape,
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = MaterialTheme.colors.background.backgroundElevation(),
            ),
            onClick = onClick,
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(min = 48.dp),
            content = {
                Text(text = text)
            },
        )

        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp),
        )
    }
}
