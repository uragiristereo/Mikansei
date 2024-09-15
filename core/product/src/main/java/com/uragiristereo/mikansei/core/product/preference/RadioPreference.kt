package com.uragiristereo.mikansei.core.product.preference

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.product.theme.MikanseiTheme

@Composable
internal fun RadioPreferenceItem(
    title: String,
    subtitle: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                interactionSource = interactionSource,
                indication = LocalIndication.current,
            )
            .padding(
                start = 4.dp,
                end = 16.dp,
            )
            .padding(
                vertical = when (subtitle) {
                    null -> 2.dp
                    else -> 8.dp
                },
            ),
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            interactionSource = interactionSource,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colors.secondaryVariant,
            ),
        )

        Column(
            modifier = Modifier
                .padding(start = 20.dp)
                .weight(weight = 1f, fill = true),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.body1,
            )

            subtitle?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium),
                )
            }
        }
    }
}

@Preview
@Composable
private fun RadioPreferenceItemPreview() {
    var selectedItem by remember { mutableStateOf(0) }

    MikanseiTheme {
        Surface(color = MaterialTheme.colors.background) {
            Column {
                RadioPreferenceItem(
                    title = "Lorem ipsum",
                    subtitle = "lorem",
                    selected = selectedItem == 0,
                    onClick = {
                        selectedItem = 0
                    },
                )

                RadioPreferenceItem(
                    title = "Dolor sit amet",
                    subtitle = "lorem",
                    selected = selectedItem == 1,
                    onClick = {
                        selectedItem = 1
                    },
                )

                RadioPreferenceItem(
                    title = "Hello",
                    subtitle = "lorem",
                    selected = selectedItem == 2,
                    onClick = {
                        selectedItem = 2
                    },
                )
            }
        }
    }
}
