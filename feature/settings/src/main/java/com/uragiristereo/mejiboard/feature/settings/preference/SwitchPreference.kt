package com.uragiristereo.mejiboard.feature.settings.preference

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp

@Composable
fun SwitchPreference(
    state: SwitchPreferenceState,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    enabled: Boolean = true,
) {
    SwitchPreference(
        state = state,
        title = title,
        subtitle = AnnotatedString(text = subtitle),
        modifier = modifier,
        icon = icon,
        enabled = enabled,
    )
}

@Composable
fun SwitchPreference(
    state: SwitchPreferenceState,
    title: String,
    subtitle: AnnotatedString?,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    enabled: Boolean = true,
) {
    val iconPadding = LocalIconPadding.current
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                enabled = enabled,
                onClick = {
                    state.set(!state.selected)
                },
            )
            .padding(
                horizontal = 16.dp,
                vertical = when (subtitle) {
                    null -> 4.dp
                    else -> 16.dp
                },
            ),
    ) {
        if (icon != null) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface.copy(
                    alpha = when {
                        enabled -> 1f
                        else -> ContentAlpha.disabled
                    }
                ),
                modifier = Modifier.padding(end = 32.dp),
            )
        }

        if (icon == null && iconPadding) {
            Spacer(
                modifier = Modifier.padding(end = 56.dp),
            )
        }

        PreferenceContainer(
            title = title,
            subtitle = subtitle,
            enabled = enabled,
            modifier = Modifier.weight(1f),
        )

        Switch(
            checked = state.selected,
            enabled = enabled,
            onCheckedChange = { state.set(it) },
            interactionSource = interactionSource,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}

@Composable
fun SwitchPreference(
    title: String,
    subtitle: String,
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    enabled: Boolean = true,
) {
    SwitchPreference(
        title = title,
        subtitle = AnnotatedString(text = subtitle),
        selected = selected,
        onSelectedChange = onSelectedChange,
        modifier = modifier,
        icon = icon,
        enabled = enabled,
    )
}

@Composable
fun SwitchPreference(
    title: String,
    subtitle: AnnotatedString?,
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    enabled: Boolean = true,
) {
    val iconPadding = LocalIconPadding.current
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                enabled = enabled,
                onClick = {
                    onSelectedChange(!selected)
                },
            )
            .padding(
                horizontal = 16.dp,
                vertical = when (subtitle) {
                    null -> 4.dp
                    else -> 16.dp
                },
            ),
    ) {
        if (icon != null) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface.copy(
                    alpha = when {
                        enabled -> 1f
                        else -> ContentAlpha.disabled
                    }
                ),
                modifier = Modifier.padding(end = 32.dp),
            )
        }

        if (icon == null && iconPadding) {
            Spacer(
                modifier = Modifier.padding(end = 56.dp),
            )
        }

        PreferenceContainer(
            title = title,
            subtitle = subtitle,
            enabled = enabled,
            modifier = Modifier.weight(1f),
        )

        Switch(
            checked = selected,
            enabled = enabled,
            onCheckedChange = onSelectedChange,
            interactionSource = interactionSource,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}

@Stable
class SwitchPreferenceState(
    val selected: Boolean,
    val onStateChange: (Boolean) -> Unit,
) {
    fun set(value: Boolean) {
        onStateChange(value)
    }

    companion object {
        fun saver(
            onStateChange: (Boolean) -> Unit,
        ): Saver<SwitchPreferenceState, *> {
            return Saver(
                save = { it.selected },
                restore = {
                    SwitchPreferenceState(
                        selected = it,
                        onStateChange = onStateChange,
                    )
                },
            )
        }
    }
}

@Composable
fun rememberSwitchPreferenceState(
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
): SwitchPreferenceState {
    return rememberSaveable(
        inputs = arrayOf(selected),
        saver = SwitchPreferenceState.saver(
            onStateChange = onSelectedChange,
        ),
        init = {
            SwitchPreferenceState(
                selected = selected,
                onStateChange = onSelectedChange,
            )
        },
    )
}
