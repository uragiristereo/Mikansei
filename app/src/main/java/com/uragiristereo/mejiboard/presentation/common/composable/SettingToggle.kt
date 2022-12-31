package com.uragiristereo.mejiboard.presentation.common.composable

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uragiristereo.mejiboard.R
import com.uragiristereo.mejiboard.presentation.common.theme.MejiboardTheme
import com.uragiristereo.mejiboard.presentation.common.theme.Theme

@Composable
fun SettingToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = when {
                    checked -> MaterialTheme.colors.primary
                    else -> MaterialTheme.colors.onSurface.copy(alpha = 0.4f)
                },
            )
            .clickable(
                onClick = {
                    onCheckedChange(!checked)
                },
                interactionSource = interactionSource,
                indication = LocalIndication.current,
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.Center)
                .widthIn(max = 620.dp)
                .padding(
                    vertical = 4.dp,
                    horizontal = 16.dp,
                )
                .padding(start = 58.dp),
        ) {
            Text(
                text = when {
                    checked -> stringResource(id = R.string.enabled_action)
                    else -> stringResource(id = R.string.disabled_action)
                },
                color = when {
                    MaterialTheme.colors.isLight && !checked -> Color.Unspecified
                    else -> MaterialTheme.colors.background
                },
                modifier = Modifier
                    .padding(end = 8.dp)
                    .weight(1f),
            )

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                interactionSource = interactionSource,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colors.background,
                )
            )
        }
    }
}

@Preview
@Composable
private fun SettingTogglePreview() {
    MejiboardTheme {
        Surface {
            val checked1 = remember { mutableStateOf(false) }
            val checked2 = remember { mutableStateOf(false) }

            Column {
                SettingToggle(
                    checked = checked1.value,
                    onCheckedChange = {
                        checked1.value = it
                    },
                )

                MejiboardTheme(
                    theme = Theme.DARK,
                ) {
                    Surface {
                        SettingToggle(
                            checked = checked2.value,
                            onCheckedChange = {
                                checked2.value = it
                            },
                        )
                    }
                }

                SettingTip(
                    text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                )
            }
        }
    }
}
