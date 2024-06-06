package com.uragiristereo.mikansei.feature.user.deactivation.in_app

import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.uragiristereo.mikansei.core.product.component.ProductAlertDialog

@Composable
fun UserDeactivationInAppConfirmationDialog(
    isVisible: Boolean,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (isVisible) {
        ProductAlertDialog(
            onDismissRequest = onDismissRequest,
            buttons = {
                TextButton(
                    onClick = onDismissRequest,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colors.primary.copy(alpha = 0.74f),
                    ),
                    content = {
                        Text(text = "Cancel".uppercase())
                    },
                )

                TextButton(
                    onClick = {
                        onDismissRequest()
                        onConfirm()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colors.error,
                    ),
                    content = {
                        Text(text = "Confirm".uppercase())
                    },
                )
            },
            title = {
                Text(text = "Confirmation")
            },
            text = {
                Text(text = "Are you sure want to deactivate your account?\nThis action cannot be undone.")
            },
            modifier = modifier,
        )
    }
}
