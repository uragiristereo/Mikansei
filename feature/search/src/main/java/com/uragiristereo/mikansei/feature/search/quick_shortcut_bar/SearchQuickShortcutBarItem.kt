package com.uragiristereo.mikansei.feature.search.quick_shortcut_bar

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.ui.extension.backgroundElevation

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
