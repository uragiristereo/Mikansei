package com.uragiristereo.mikansei.feature.user.login.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.uragiristereo.mikansei.core.ui.extension.backgroundElevation

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun LoginDialog(
    state: LoginState,
    onDismiss: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state == LoginState.LoggingIn || state is LoginState.Failed) {
        AlertDialog(
            backgroundColor = MaterialTheme.colors.background.backgroundElevation(3.dp),
            properties = DialogProperties(
                dismissOnBackPress = state != LoginState.LoggingIn,
                dismissOnClickOutside = state != LoginState.LoggingIn,
                usePlatformDefaultWidth = false,
            ),
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = when (state) {
                        LoginState.LoggingIn -> "Logging in..."
                        is LoginState.Failed -> "Login failed!"
                        else -> ""
                    },
                )
            },
            text = {
                when (state) {
                    LoginState.LoggingIn -> LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    is LoginState.Failed -> Text(text = state.message)
                    else -> {}
                }
            },
            buttons = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        space = 16.dp,
                        alignment = Alignment.End,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 12.dp,
                        ),
                ) {
                    when (state) {
                        LoginState.LoggingIn ->
                            TextButton(
                                onClick = onCancelClick,
                                content = {
                                    Text(text = "Cancel".uppercase())
                                },
                            )
                        is LoginState.Failed ->
                            TextButton(
                                onClick = onDismiss,
                                content = {
                                    Text(text = "Ok".uppercase())
                                },
                            )
                        else -> {}
                    }
                }
            },
            modifier = modifier.fillMaxWidth(0.9f),
        )
    }
}
