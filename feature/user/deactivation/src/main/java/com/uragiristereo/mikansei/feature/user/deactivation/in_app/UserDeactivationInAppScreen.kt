package com.uragiristereo.mikansei.feature.user.deactivation.in_app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.product.theme.MikanseiTheme
import com.uragiristereo.mikansei.core.product.theme.Theme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun UserDeactivationInAppScreen(
    innerPadding: PaddingValues,
    isLoading: Boolean,
    password: TextFieldValue,
    onPasswordChange: (TextFieldValue) -> Unit,
    onDeactivateClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(innerPadding)
            .padding(all = 16.dp),
    ) {
        Text(
            text = "To confirm your identity, enter your password below to deactivate your account.",
            style = MaterialTheme.typography.body2,
        )

        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            enabled = !isLoading,
            value = password,
            onValueChange = onPasswordChange,
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Default,
                keyboardType = KeyboardType.Password,
                autoCorrectEnabled = false,
            ),
            label = {
                Text(text = "Password")
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "This action cannot be undone and your account cannot be recovered after it is " +
                    "deactivated.",
            style = MaterialTheme.typography.body2,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            enabled = password.text.isNotBlank() && !isLoading,
            onClick = {
                focusRequester.freeFocus()
                onDeactivateClick()
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.error,
            ),
            content = {
                Text(
                    text = when {
                        !isLoading -> "Deactivate now"
                        else -> "Deactivating..."
                    }.uppercase(),
                )
            },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(8.dp))
    }

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }
}

@Preview
@Composable
private fun UserDeactivationInAppScreenPreview() {
    val scope = rememberCoroutineScope()
    var password by remember { mutableStateOf(TextFieldValue()) }
    var isLoading by remember { mutableStateOf(false) }

    MikanseiTheme(theme = Theme.DARK) {
        Surface {
            UserDeactivationInAppScreen(
                innerPadding = PaddingValues(),
                isLoading = isLoading,
                password = password,
                onPasswordChange = { password = it },
                onDeactivateClick = {
                    scope.launch {
                        isLoading = true
                        delay(timeMillis = 2_000)
                        isLoading = false
                    }
                },
            )
        }
    }
}
