package com.uragiristereo.mikansei.feature.settings.preference

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.preferences.model.base.Preference
import com.uragiristereo.mikansei.core.preferences.model.base.PreferenceString
import com.uragiristereo.mikansei.core.preferences.model.base.PreferenceStringRes

@Composable
fun Preference.getTitleString(): String {
    return when (this) {
        is PreferenceStringRes -> stringResource(titleResId)
        is PreferenceString -> title
        else -> ""
    }
}

@Composable
fun Preference.getSubtitleString(): String? {
    return when (this) {
        is PreferenceStringRes -> subtitleResId?.let { stringResource(it) }
        is PreferenceString -> subtitle
        else -> null
    }
}

@Composable
internal fun <T : Preference> DropDownPreference(
    state: DropDownPreferenceState<T>,
    title: String,
    icon: Painter?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val iconPadding = LocalIconPadding.current

    var dropDownExpanded by rememberSaveable { mutableStateOf(value = false) }

    RegularPreference(
        title = title,
        subtitle = state.selectedItem?.getTitleString().orEmpty(),
        icon = icon,
        onClick = {
            dropDownExpanded = true
        },
        modifier = modifier
            .background(
                color = when {
                    dropDownExpanded -> MaterialTheme.colors.primary.copy(alpha = 0.2f)
                    else -> Color.Unspecified
                },
            ),
        enabled = enabled,
    )

    Box(
        modifier = Modifier
            .padding(
                start = when {
                    iconPadding -> 24.dp + 32.dp
                    else -> 0.dp
                },
            ),
    ) {
        DropdownMenu(
            expanded = dropDownExpanded,
            onDismissRequest = { dropDownExpanded = !dropDownExpanded },
        ) {
            state.items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        dropDownExpanded = false

                        state.set(item)
                    },
                    modifier = Modifier
                        .background(
                            color = when (state.selectedItem) {
                                item -> MaterialTheme.colors.primary.copy(alpha = 0.4f)
                                else -> Color.Unspecified
                            },
                        ),
                    content = {
                        Text(
                            text = item.getTitleString(),
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                )
            }
        }
    }
}

@Stable
class DropDownPreferenceState<T : Preference>(
    val items: Array<T>,
    val selectedItem: T?,
    val onItemSelected: (T) -> Unit,
) {
    fun set(item: T) {
        onItemSelected(item)
    }

    companion object {
        fun <T : Preference> saver(
            items: Array<T>,
            onItemSelected: (T) -> Unit,
        ): Saver<DropDownPreferenceState<T>, *> {
            return Saver(
                save = {
                    it.selectedItem
                },
                restore = {
                    DropDownPreferenceState(
                        items = items,
                        selectedItem = it,
                        onItemSelected = onItemSelected,
                    )
                },
            )
        }
    }
}

@Composable
internal fun <T : Preference> rememberDropDownPreferenceState(
    items: Array<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
): DropDownPreferenceState<T> {
    return rememberSaveable(
        inputs = arrayOf(
            items,
            selectedItem,
        ),
        saver = DropDownPreferenceState.saver(
            items = items,
            onItemSelected = onItemSelected,
        ),
        init = {
            DropDownPreferenceState(
                items = items,
                selectedItem = selectedItem,
                onItemSelected = onItemSelected,
            )
        },
    )
}

//@Composable
//internal fun rememberDropDownPreferenceState(
//    preference: List<Preference>,
//    selectedItem: Preference,
//    onItemSelected: (Preference) -> Unit,
//): DropDownPreferenceState {
//    return rememberDropDownPreferenceState(
//        items = preference.items,
//        selectedItem = selectedItem,
//        onItemSelected = onItemSelected,
//    )
//}
