package com.uragiristereo.mikansei.feature.settings.preference

import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.preferences.model.base.Preference
import com.uragiristereo.mikansei.core.product.component.ProductModalBottomSheet
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.composable.DragHandle
import com.uragiristereo.mikansei.core.ui.composable.NavigationBarSpacer
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun BottomSheetPreference(
    state: BottomSheetPreferenceState,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()

    ProductModalBottomSheet(
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
                        title = item.getTitleString(),
                        subtitle = item.getSubtitleString(),
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
data class BottomSheetPreferenceData<T : Preference>(
    @StringRes val preferenceTextResId: Int = R.string.app_name,
    val items: @RawValue List<T> = listOf(),
    val selectedItem: @RawValue T? = null,
) : Parcelable

@OptIn(ExperimentalMaterialApi::class)
@Suppress("UNCHECKED_CAST")
@Stable
class BottomSheetPreferenceState(
    val sheetState: ModalBottomSheetState,
    val onItemSelected: (Preference) -> Unit,
) {
    var data by mutableStateOf(BottomSheetPreferenceData<Preference>())

    suspend fun <T : Preference> navigate(data: BottomSheetPreferenceData<T>) {
        this.data = data as BottomSheetPreferenceData<Preference>

        sheetState.animateTo(targetValue = ModalBottomSheetValue.Expanded)
    }

    suspend fun set(item: Preference) {
        data = data.copy(selectedItem = item)

        onItemSelected(item)

        sheetState.hide()
    }

    companion object {
        fun saver(
            sheetState: ModalBottomSheetState,
            onItemSelected: (Preference) -> Unit,
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
internal fun rememberBottomSheetPreferenceState(
    sheetState: ModalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden),
    onItemSelected: (Preference) -> Unit,
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
