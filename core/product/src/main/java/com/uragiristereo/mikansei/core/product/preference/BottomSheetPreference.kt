package com.uragiristereo.mikansei.core.product.preference

import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import com.uragiristereo.mikansei.core.model.preferences.base.Preference
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.composable.NavigationBarSpacer
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.ModalBottomSheet3
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.SheetState3
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.rememberModalBottomSheetState3
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Composable
fun BottomSheetPreference(
    state: BottomSheetPreferenceState,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()

    ModalBottomSheet3(
        sheetState = state.sheetState,
        modifier = modifier,
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
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
        }
    }
}

@Parcelize
data class BottomSheetPreferenceData<T : Preference>(
    @StringRes val preferenceTextResId: Int = R.string.app_name,
    val items: @RawValue List<T> = listOf(),
    val selectedItem: @RawValue T? = null,
) : Parcelable

@Suppress("UNCHECKED_CAST")
@Stable
class BottomSheetPreferenceState(
    val sheetState: SheetState3,
    val onItemSelected: (Preference) -> Unit,
) {
    var data by mutableStateOf(BottomSheetPreferenceData<Preference>())

    suspend fun <T : Preference> navigate(data: BottomSheetPreferenceData<T>) {
        this.data = data as BottomSheetPreferenceData<Preference>

        sheetState.show()
    }

    suspend fun set(item: Preference) {
        data = data.copy(selectedItem = item)

        onItemSelected(item)

        sheetState.hide()
    }

    companion object {
        fun saver(
            sheetState: SheetState3,
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

@Composable
fun rememberBottomSheetPreferenceState(
    sheetState: SheetState3 = rememberModalBottomSheetState3(),
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
