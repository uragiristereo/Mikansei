package com.uragiristereo.mejiboard.feature.search.core

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.uragiristereo.mejiboard.R
import com.uragiristereo.mejiboard.core.common.ui.extension.backgroundElevation

@Composable
fun SearchBar(
    query: TextFieldValue,
    actionsRowExpanded: Boolean,
    loading: Boolean,
    onQueryChange: (TextFieldValue) -> Unit,
    onQuerySubmit: (TextFieldValue) -> Unit,
    onNavigateBack: () -> Unit,
    onClearClick: () -> Unit,
    onActionsRowExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    focusRequester: FocusRequester = remember { FocusRequester() },
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = {
                Text(text = placeholder)
            },
            leadingIcon = {
                IconButton(
                    onClick = onNavigateBack,
                    content = {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back),
                            contentDescription = null,
                        )
                    },
                )
            },
            trailingIcon = {
                Row {
                    if (query.text.isNotEmpty()) {
                        IconButton(
                            onClick = onClearClick,
                            content = {
                                AnimatedVisibility(
                                    visible = loading,
                                    enter = fadeIn(),
                                    exit = fadeOut(),
                                    content = {
                                        CircularProgressIndicator(
                                            strokeWidth = 2.dp,
                                            color = MaterialTheme.colors.secondary,
                                            modifier = Modifier.size(26.dp),
                                        )
                                    },
                                )

                                Icon(
                                    painter = painterResource(id = R.drawable.close),
                                    contentDescription = stringResource(id = R.string.clear_action),
                                )
                            },
                        )
                    }

                    IconButton(
                        onClick = {
                            onActionsRowExpandedChange(!actionsRowExpanded)
                        },
                        content = {
                            Icon(
                                painter = painterResource(
                                    id = when {
                                        actionsRowExpanded -> R.drawable.expand_less
                                        else -> R.drawable.expand_more
                                    },
                                ),
                                contentDescription = null,
                            )
                        },
                    )
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                backgroundColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
                keyboardType = KeyboardType.Uri,
                autoCorrect = false,
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onQuerySubmit(query)
                },
            ),
            singleLine = true,
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colors.background.backgroundElevation(),
                )
                .focusRequester(focusRequester),
        )

        Divider()
    }
}
