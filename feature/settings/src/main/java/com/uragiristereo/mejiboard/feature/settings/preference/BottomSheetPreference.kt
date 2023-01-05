package com.uragiristereo.mejiboard.feature.settings.preference

import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.uragiristereo.mejiboard.core.common.ui.composable.DragHandle
import com.uragiristereo.mejiboard.core.common.ui.composable.NavigationBarSpacer
import com.uragiristereo.mejiboard.core.model.preferences.PreferenceItem
import com.uragiristereo.mejiboard.core.resources.R
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetPreference(
    state: BottomSheetPreferenceState,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()

    com.uragiristereo.mejiboard.core.product.component.ProductModalBottomSheet(
        sheetState = state.sheetState,
        modifier = modifier,
        content = {
            DragHandle()

            Text(
                text = stringResource(id = state.data.preferenceTextResId),
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp,
                        bottom = 8.dp,
                    ),
            )

            LazyColumn {
                items(state.data.items) { item ->
                    RadioPreferenceItem(
                        title = stringResource(id = item.titleResId),
                        subtitle = item.subtitleResId?.let { stringResource(id = it) },
                        selected = state.data.selectedItem == item,
                        onClick = {
                            scope.launch {
                                state.set(item = item)
                            }
                        },
                    )
                }

                item {
                    NavigationBarSpacer()
                }
            }
        },
    )
}

@Parcelize
data class BottomSheetPreferenceData(
    val preferenceKey: String = "",
    @StringRes val preferenceTextResId: Int = R.string.app_name,
    val items: List<PreferenceItem> = listOf(),
    val selectedItem: PreferenceItem? = null,
) : Parcelable

@OptIn(ExperimentalMaterialApi::class)
@Stable
class BottomSheetPreferenceState(
    val sheetState: ModalBottomSheetState,
    val onItemSelected: (preferenceKey: String, selectedItem: PreferenceItem?) -> Unit,
) {
    var data by mutableStateOf(BottomSheetPreferenceData())

    suspend fun navigate(data: BottomSheetPreferenceData) {
        this.data = data

        sheetState.animateTo(targetValue = ModalBottomSheetValue.Expanded)
    }

    suspend fun set(item: PreferenceItem) {
        data = data.copy(selectedItem = item)

        onItemSelected(data.preferenceKey, data.selectedItem)

        sheetState.hide()
    }

    companion object {
        fun saver(
            sheetState: ModalBottomSheetState,
            onItemSelected: (preferenceKey: String, selectedItem: PreferenceItem?) -> Unit,
        ): Saver<BottomSheetPreferenceState, *> {
            return Saver(
                save = {
                    it.data
                },
                restore = {
                    val state = BottomSheetPreferenceState(
                        sheetState = sheetState,
                        onItemSelected = onItemSelected,
                    )

                    state.data = it

                    state
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun rememberBottomSheetPreferenceState(
    sheetState: ModalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden),
    onItemSelected: (preferenceKey: String, selectedItem: PreferenceItem?) -> Unit,
): BottomSheetPreferenceState {
    return rememberSaveable(
        inputs = arrayOf(
            sheetState,
            onItemSelected,
        ),
        saver = BottomSheetPreferenceState.saver(
            sheetState = sheetState,
            onItemSelected = onItemSelected,
        ),
        init = {
            BottomSheetPreferenceState(
                sheetState = sheetState,
                onItemSelected = onItemSelected,
            )
        },
    )
}
