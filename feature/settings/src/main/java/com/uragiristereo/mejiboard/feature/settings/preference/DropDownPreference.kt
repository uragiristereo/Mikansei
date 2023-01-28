package com.uragiristereo.mejiboard.feature.settings.preference

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.uragiristereo.mejiboard.core.model.preferences.PreferenceItem
import com.uragiristereo.mejiboard.core.network.model.BasePreference

@Composable
fun DropDownPreference(
    state: DropDownPreferenceState,
    title: String,
    icon: Painter?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val iconPadding = LocalIconPadding.current

    var dropDownExpanded by rememberSaveable { mutableStateOf(value = false) }

    RegularPreference(
        title = title,
        subtitle = state.selectedItem?.titleResId?.let { stringResource(id = it) }.orEmpty(),
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
                            text = stringResource(id = item.titleResId),
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                )
            }
        }
    }
}

@Stable
class DropDownPreferenceState(
    val items: List<PreferenceItem>,
    val selectedItem: PreferenceItem?,
    val onItemSelected: (PreferenceItem) -> Unit,
) {
    fun set(item: PreferenceItem) {
        onItemSelected(item)
    }

    companion object {
        fun saver(
            items: List<PreferenceItem>,
            onItemSelected: (PreferenceItem) -> Unit,
        ): Saver<DropDownPreferenceState, *> {
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
fun rememberDropDownPreferenceState(
    items: List<PreferenceItem>,
    selectedItem: PreferenceItem?,
    onItemSelected: (PreferenceItem) -> Unit,
): DropDownPreferenceState {
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

@Composable
fun rememberDropDownPreferenceState(
    preference: BasePreference,
    selectedItemKey: String,
    onItemSelected: (PreferenceItem) -> Unit,
): DropDownPreferenceState {
    val selectedItem by remember(key1 = selectedItemKey) {
        mutableStateOf(preference.getItemByKey(selectedItemKey))
    }

    return rememberDropDownPreferenceState(
        items = preference.items,
        selectedItem = selectedItem,
        onItemSelected = onItemSelected,
    )
}
