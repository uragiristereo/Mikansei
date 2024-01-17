package com.uragiristereo.mikansei.feature.home.favorites.favgroup.edit

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
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.ui.extension.strip
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.feature.home.favorites.favgroup.new.core.FabState
import com.uragiristereo.serializednavigationextension.runtime.navArgsOf
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@OptIn(SavedStateHandleSaveableApi::class)
class EditFavGroupViewModel(
    savedStateHandle: SavedStateHandle,
    private val danbooruRepository: DanbooruRepository,
) : ViewModel() {
    private val navArgs = checkNotNull(savedStateHandle.navArgsOf<HomeRoute.EditFavoriteGroup>())
    val favoriteGroup = navArgs.favoriteGroup
    private val spaceSeparatedPostIds = favoriteGroup.postIds.joinToString(separator = " ")

    private val channel = Channel<Event>()
    val event = channel.receiveAsFlow()

    private var isLoading by mutableStateOf(false)

    private val nameTextFieldDefault: TextFieldValue
        get() = TextFieldValue(
            text = favoriteGroup.name,
            selection = TextRange(favoriteGroup.name.length),
        )

    private val postIdsTextFieldDefault: TextFieldValue
        get() = TextFieldValue(
            text = spaceSeparatedPostIds,
            selection = TextRange(spaceSeparatedPostIds.length),
        )

    var nameTextField by savedStateHandle.saveable(
        stateSaver = TextFieldValue.Saver,
        init = {
            mutableStateOf(nameTextFieldDefault)
        },
    )
        private set

    var postIdsTextField by savedStateHandle.saveable(
        stateSaver = TextFieldValue.Saver,
        init = {
            mutableStateOf(postIdsTextFieldDefault)
        },
    )
        private set

    val isNameEdited by derivedStateOf {
        nameTextField.text != favoriteGroup.name
    }

    val arePostIdsEdited by derivedStateOf {
        postIdsTextField.text.strip(splitter = " ") != spaceSeparatedPostIds
    }

    private val arePostIdsValid by derivedStateOf {
        runCatching {
            mapPostIds()

            true
        }.getOrElse {
            false
        }
    }

    val fabState by derivedStateOf {
        when {
            isLoading -> FabState.LOADING
            (isNameEdited || arePostIdsEdited) && nameTextField.text.isNotBlank() && arePostIdsValid -> FabState.ENABLED
            else -> FabState.HIDDEN
        }
    }

    var unsavedConfirmationDialogVisible by mutableStateOf(false)
        private set

    fun editFavoriteGroup() {
        viewModelScope.launch {
            isLoading = true

            val result = danbooruRepository.editFavoriteGroup(
                favoriteGroupId = favoriteGroup.id,
                name = nameTextField.text,
                postIds = mapPostIds(),
            )

            when (result) {
                is Result.Success -> channel.send(Event.OnSuccess)
                is Result.Failed -> channel.send(Event.OnFailed(result.message))
                is Result.Error -> channel.send(Event.OnFailed(result.t.toString()))
            }

            isLoading = false
        }
    }

    fun onNameTextFieldChange(value: TextFieldValue) {
        nameTextField = value
    }

    fun onPostIdsTextFieldChange(value: TextFieldValue) {
        postIdsTextField = value
    }

    fun onUndoClick() {
        nameTextField = nameTextFieldDefault
        postIdsTextField = postIdsTextFieldDefault
    }

    fun showUnsavedConfirmationDialog() {
        unsavedConfirmationDialogVisible = true
    }

    fun hideUnsavedConfirmationDialog() {
        unsavedConfirmationDialogVisible = false
    }

    private fun mapPostIds(): List<Int> {
        return postIdsTextField.text
            .strip(splitter = " ")
            .split(" ")
            .map {
                it.toInt()
            }
    }

    sealed interface Event {
        data object OnSuccess : Event
        data class OnFailed(val message: String) : Event
    }
}
