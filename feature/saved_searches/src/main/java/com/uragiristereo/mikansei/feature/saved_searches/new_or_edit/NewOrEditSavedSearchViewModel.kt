package com.uragiristereo.mikansei.feature.saved_searches.new_or_edit

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.SavedSearch
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.ui.extension.strip
import com.uragiristereo.mikansei.core.ui.navigation.SavedSearchesRoute
import com.uragiristereo.mikansei.feature.saved_searches.new_or_edit.core.FabState
import com.uragiristereo.serializednavigationextension.runtime.navArgsOf
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(SavedStateHandleSaveableApi::class)
class NewOrEditSavedSearchViewModel(
    savedStateHandle: SavedStateHandle,
    private val danbooruRepository: DanbooruRepository,
) : ViewModel() {
    private val navArgs = checkNotNull(
        savedStateHandle.navArgsOf<SavedSearchesRoute.NewOrEdit>()
    )
    val savedSearchId = navArgs.savedSearch?.id
    val shouldFocusToLabelsTextField = navArgs.query != null
    private val defaultQuery = navArgs.query ?: navArgs.savedSearch?.query ?: ""
    private val spaceSeparatedLabels = navArgs.savedSearch?.labels
        ?.joinToString(separator = " ") ?: ""

    private val channel = Channel<Event>()
    val event = channel.receiveAsFlow()

    private var isLoading by mutableStateOf(false)
    var isUnsavedConfirmationDialogVisible by mutableStateOf(false); private set

    var queryTextField by savedStateHandle.saveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue(
                text = defaultQuery,
                selection = TextRange(index = defaultQuery.length),
            )
        )
    }
        private set

    var labelsTextField by savedStateHandle.saveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue(
                text = spaceSeparatedLabels,
                selection = TextRange(index = spaceSeparatedLabels.length),
            )
        )
    }
        private set

    val isQueryEdited by derivedStateOf {
        queryTextField.text != defaultQuery
    }

    val areLabelsEdited by derivedStateOf {
        labelsTextField.text.strip(splitter = " ") != spaceSeparatedLabels
                || spaceSeparatedLabels == ""
    }

    val fabState by derivedStateOf {
        when {
            isLoading -> FabState.LOADING
            (isQueryEdited || areLabelsEdited) && queryTextField.text.isNotBlank() -> FabState.ENABLED
            else -> FabState.HIDDEN
        }
    }

    fun submit() {
        viewModelScope.launch {
            isLoading = true

            val result = when {
                savedSearchId != null -> {
                    danbooruRepository.editSavedSearch(
                        savedSearch = SavedSearch(
                            id = savedSearchId,
                            query = queryTextField.text,
                            labels = labelsTextField.text.split(" "),
                        )
                    )
                }

                else -> {
                    danbooruRepository.createNewSavedSearch(
                        query = queryTextField.text,
                        labels = labelsTextField.text.split(" "),
                    )
                }
            }

            when (result) {
                is Result.Success -> channel.send(Event.OnSuccess)

                is Result.Failed -> {
                    Timber.d(result.message)
                    channel.send(Event.OnFailed(result.message))
                }

                is Result.Error -> {
                    Timber.d((result.t))
                    channel.send(Event.OnFailed(result.t.toString()))
                }
            }

            isLoading = false
        }
    }

    fun onQueryTextFieldChange(value: TextFieldValue) {
        queryTextField = value
    }

    fun onLabelsTextFieldChange(value: TextFieldValue) {
        labelsTextField = value
    }

    fun showUnsavedConfirmationDialog() {
        isUnsavedConfirmationDialogVisible = true
    }

    fun hideUnsavedConfirmationDialog() {
        isUnsavedConfirmationDialogVisible = false
    }

    sealed interface Event {
        data object OnSuccess : Event
        data class OnFailed(val message: String) : Event
    }
}
