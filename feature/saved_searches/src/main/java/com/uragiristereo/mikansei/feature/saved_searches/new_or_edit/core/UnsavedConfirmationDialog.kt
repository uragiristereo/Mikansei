package com.uragiristereo.mikansei.feature.saved_searches.new_or_edit.core

import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import com.uragiristereo.mikansei.core.product.component.ProductAlertDialog

@Composable
fun UnsavedConfirmationDialog(
    visible: Boolean,
    onDiscard: () -> Unit,
    onHide: () -> Unit,
) {
    if (visible) {
        ProductAlertDialog(
            title = {
                Text(text = "Exit confirmation")
            },
            text = {
                Text(text = "You have unsaved changes, do you want to discard them and exit?")
            },
            onDismissRequest = {},
            buttons = {
                TextButton(
                    onClick = onHide,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colors.primary.copy(alpha = 0.74f),
                    ),
                    content = {
                        Text(text = "Cancel".uppercase())
                    },
                )

                TextButton(
                    onClick = {
                        onHide()
                        onDiscard()
                    },
                    content = {
                        Text(text = "Discard".uppercase())
                    },
                )
            },
        )
    }
}
