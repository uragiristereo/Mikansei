package com.uragiristereo.mikansei.feature.search.core

import android.view.inputmethod.EditorInfo
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.extension.backgroundElevation
import com.uragiristereo.mikansei.core.ui.view.SelectionAwareEditText
import com.uragiristereo.mikansei.core.ui.view.SelectionTextWatcher

@Composable
internal fun SearchBar(
    editText: SelectionAwareEditText,
    query: TextFieldValue,
    loading: Boolean,
    onQueryChange: (TextFieldValue) -> Unit,
    onQuerySubmit: (TextFieldValue) -> Unit,
    onNavigateBack: () -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
) {
    val contentColor = MaterialTheme.colors.onSurface
    val highContentColor = contentColor.copy(alpha = ContentAlpha.high)
    val mediumContentColor = contentColor.copy(alpha = ContentAlpha.medium)

    val textWatcher = remember {
        object : SelectionTextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onQueryChange(
                    TextFieldValue(
                        text = s?.toString().orEmpty(),
                        selection = TextRange(
                            start = editText.selectionStart,
                            end = editText.selectionEnd,
                        )
                    )
                )
            }

            override fun onSelectionChanged(selStart: Int, selEnd: Int) {
                onQueryChange(
                    TextFieldValue(
                        text = editText.text?.toString().orEmpty(),
                        selection = TextRange(
                            start = selStart,
                            end = selEnd,
                        )
                    )
                )
            }
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(56.dp)
                .background(
                    color = MaterialTheme.colors.background.backgroundElevation(),
                )
                .padding(horizontal = 4.dp),
        ) {
            IconButton(
                onClick = onNavigateBack,
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_back),
                        contentDescription = null,
                        tint = highContentColor,
                    )
                },
            )

            AndroidView(
                factory = {
                    editText.apply {
                        addSelectionTextChangedListener(textWatcher)
                        setOnEditorActionListener { _, actionId, _ ->
                            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                                onQuerySubmit(query)
                                return@setOnEditorActionListener true
                            }

                            false
                        }
                    }
                },
                update = {
                    it.apply {
                        setHint(placeholder)
                        setTextColor(highContentColor.toArgb())
                        setHintTextColor(mediumContentColor.toArgb())

                        val value = TextFieldValue(
                            text = text.toString(),
                            selection = TextRange(
                                start = selectionStart,
                                end = selectionEnd
                            ),
                        )
                        if (query.text != value.text) {
                            editText.removeSelectionTextChangedListener(textWatcher)
                            setText(query.text)
                            setSelection(query.selection.start, query.selection.end)
                            editText.addSelectionTextChangedListener(textWatcher)
                        } else {
                            if (query.selection != value.selection) {
                                editText.removeSelectionTextChangedListener(textWatcher)
                                setSelection(query.selection.start, query.selection.end)
                                editText.addSelectionTextChangedListener(textWatcher)
                            }
                        }
                    }
                },
                modifier = Modifier
                    .padding(4.dp)
                    .padding(start = 16.dp)
                    .weight(1f, fill = true),
            )

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
                            tint = mediumContentColor,
                        )
                    },
                )
            } else {
                Spacer(Modifier.size(24.dp))
            }
        }

        Divider()
    }
}
