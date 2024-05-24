package com.uragiristereo.mikansei.feature.user.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Profile
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.ProfileSettingsField
import com.uragiristereo.mikansei.core.domain.module.database.UserDelegationRepository
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.domain.usecase.SyncUserSettingsUseCase
import com.uragiristereo.mikansei.core.domain.usecase.UpdateUserSettingsUseCase
import com.uragiristereo.mikansei.core.model.Environment
import com.uragiristereo.mikansei.core.model.preferences.base.Preference
import com.uragiristereo.mikansei.core.model.preferences.user.DetailSizePreference
import com.uragiristereo.mikansei.core.model.preferences.user.RatingPreference
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.preferences.PreferencesRepository
import com.uragiristereo.mikansei.core.product.preference.BottomSheetPreferenceData
import com.uragiristereo.mikansei.core.resources.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class UserSettingsViewModel(
    private val userRepository: UserRepository,
    private val userDelegationRepository: UserDelegationRepository,
    private val preferencesRepository: PreferencesRepository,
    private val syncUserSettingsUseCase: SyncUserSettingsUseCase,
    private val updateUserSettingsUseCase: UpdateUserSettingsUseCase,
    private val environment: Environment,
) : ViewModel() {
    val activeUser: StateFlow<Profile>
        get() = userRepository.active

    var loading by mutableStateOf(true)
        private set

    var failed by mutableStateOf(false)
        private set

    var ratingFilters by mutableStateOf(
        value = BottomSheetPreferenceData(
            preferenceTextResId = R.string.settings_booru_listing_mode_select,
            items = RatingPreference.entries,
            selectedItem = activeUser.value.mikansei.postsRatingFilter,
        ),
    )
        private set

    val snackbarChannel = Channel<String>()
    private var requestSyncJob: Job? = null
    private var updateSettingsJob: Job? = null

    val safeModeEnvironment = environment.safeMode

    val yankeeFlagEnabled = preferencesRepository.data
        .map { it.flags.yankee }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 2_000L),
            initialValue = false,
        )

    val delegatedUserName = userDelegationRepository
        .getDelegatedUserById(activeUser.value.id)
        .map { delegatedUserId ->
            if (activeUser.value.id != delegatedUserId) {
                delegatedUserId
            } else {
                null
            }
        }
        .flatMapLatest { delegatedUserId ->
            if (delegatedUserId != null) {
                userRepository.get(delegatedUserId)
            } else {
                flowOf(null)
            }
        }
        .map { it?.name }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 2_000L),
            initialValue = null,
        )

    init {
        viewModelScope.launch {
            userRepository.active.collect {
                ratingFilters = ratingFilters.copy(selectedItem = it.mikansei.postsRatingFilter)
            }
        }

        requestSync()
    }

    fun requestSync() {
        requestSyncJob?.cancel()

        requestSyncJob = viewModelScope.launch {
            loading = true

            syncSettings()

            loading = false
        }
    }

    private suspend fun syncSettings() {
        when (val result = syncUserSettingsUseCase()) {
            is Result.Success -> failed = false

            is Result.Failed -> {
                failed = true
                snackbarChannel.send(result.message)
            }

            is Result.Error -> {
                failed = true
                snackbarChannel.send(result.t.toString())
            }
        }
    }

    private fun updateSettings(data: ProfileSettingsField) {
        updateSettingsJob?.cancel()

        updateSettingsJob = viewModelScope.launch {
            loading = activeUser.value.isNotAnonymous()

            updateUserSettingsUseCase(data)
            syncSettings()

            loading = false
        }
    }

    fun onSafeModeChange(value: Boolean) {
        updateSettings(data = ProfileSettingsField(enableSafeMode = value))
    }

    fun onShowDeletedPostsChange(value: Boolean) {
        updateSettings(data = ProfileSettingsField(showDeletedPosts = value))
    }

    fun onDetailSizeChange(value: DetailSizePreference) {
        updateSettings(data = ProfileSettingsField(defaultImageSize = value))
    }

    fun onShowPendingPostsChange(value: Boolean) {
        viewModelScope.launch {
            userRepository.update { profile ->
                profile.copy(
                    mikansei = profile.mikansei.copy(showPendingPosts = value),
                )
            }
        }
    }

    fun setBottomSheetPreferenceState(preference: Preference) {
        when (preference) {
            is RatingPreference -> {
                viewModelScope.launch {
                    userRepository.update { profile ->
                        profile.copy(
                            mikansei = profile.mikansei.copy(postsRatingFilter = preference),
                        )
                    }
                }
            }
        }
    }

    fun yankee() {
        viewModelScope.launch {
            preferencesRepository.update {
                it.copy(flags = it.flags.copy(yankee = !environment.safeMode))
            }
        }
    }
}
