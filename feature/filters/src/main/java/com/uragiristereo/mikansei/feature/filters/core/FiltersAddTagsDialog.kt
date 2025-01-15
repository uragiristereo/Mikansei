package com.uragiristereo.mikansei.feature.filters.core

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.product.component.ProductAlertDialog
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.extension.strip
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun FiltersAddTagsDialog(
    dialogShown: Boolean,
    onDismiss: () -> Unit,
    onDone: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()

    if (dialogShown) {
        var textField by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue())
        }
        val focusRequester = remember { FocusRequester() }
        val addButtonEnabled = remember(textField.text) {
            textField.text
                .strip(splitter = "")
                .isNotEmpty()
        }

        ProductAlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(text = stringResource(id = R.string.filters_add_new_tags))
            },
            text = {
                Column {
                    Text(
                        text = stringResource(id = R.string.filters_separate_tags),
                        modifier = Modifier.padding(bottom = 8.dp),
                    )

                    OutlinedTextField(
                        value = textField,
                        onValueChange = { textField = it },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Default,
                            keyboardType = KeyboardType.Uri,
                            autoCorrectEnabled = false,
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (addButtonEnabled) {
                                    onDone(textField.text)
                                }
                            },
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                    )

                    DisposableEffect(key1 = Unit) {
                        scope.launch {
                            delay(timeMillis = 200L)
                            focusRequester.requestFocus()
                        }

                        onDispose { }
                    }
                }
            },
            buttons = {
                TextButton(
                    onClick = {
                        onDismiss()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colors.primary
                            .copy(alpha = 0.74f),
                    ),
                    content = {
                        Text(text = stringResource(id = R.string.cancel_action).uppercase())
                    },
                )

                TextButton(
                    enabled = addButtonEnabled,
                    onClick = {
                        onDone(textField.text)
                    },
                    modifier = Modifier.padding(end = 8.dp),
                    content = {
                        Text(text = stringResource(id = R.string.add_action).uppercase())
                    },
                )
            },
            modifier = modifier,
        )
    }
}
