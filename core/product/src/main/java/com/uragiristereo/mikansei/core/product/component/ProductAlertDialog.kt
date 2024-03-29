package com.uragiristereo.mikansei.core.product.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.ui.extension.backgroundElevation

@Composable
fun ProductAlertDialog(
    onDismissRequest: () -> Unit,
    buttons: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    title: (@Composable () -> Unit)? = null,
    text: @Composable (() -> Unit)? = null,
) {
    AlertDialog(
        shape = RoundedCornerShape(size = 12.dp),
        backgroundColor = MaterialTheme.colors.background.backgroundElevation(),
        contentColor = MaterialTheme.colors.onBackground,
        onDismissRequest = onDismissRequest,
        buttons = {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 12.dp),
            ) {
                buttons(this)
            }
        },
        title = title,
        text = text,
        modifier = modifier,
    )
}
