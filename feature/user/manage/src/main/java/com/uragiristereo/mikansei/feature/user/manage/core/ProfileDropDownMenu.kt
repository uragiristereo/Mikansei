package com.uragiristereo.mikansei.feature.user.manage.core

import androidx.compose.foundation.background
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.uragiristereo.mikansei.core.ui.extension.backgroundElevation

@Composable
internal fun ProfileDropDownMenu(
    isExpanded: Boolean,
    showLogout: Boolean,
    onSetActiveClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = onDismiss,
        modifier = modifier.background(MaterialTheme.colors.background.backgroundElevation()),
    ) {
        DropdownMenuItem(
            onClick = onSetActiveClick,
            content = {
                Text(text = "Set as active")
            },
        )

        if (showLogout) {
            DropdownMenuItem(
                onClick = onLogoutClick,
                content = {
                    Text(text = "Logout")
                },
            )
        }
    }
}
