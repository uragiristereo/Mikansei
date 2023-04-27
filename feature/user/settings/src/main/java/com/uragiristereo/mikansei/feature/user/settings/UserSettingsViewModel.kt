package com.uragiristereo.mikansei.feature.user.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uragiristereo.mikansei.core.database.dao.user.UserDao
import com.uragiristereo.mikansei.core.database.dao.user.toUser
import com.uragiristereo.mikansei.core.database.dao.user.toUserRow
import com.uragiristereo.mikansei.core.domain.entity.user.UserField
import com.uragiristereo.mikansei.core.domain.usecase.SyncUserSettingsUseCase
import com.uragiristereo.mikansei.core.domain.usecase.UpdateUserSettingsUseCase
import com.uragiristereo.mikansei.core.model.preferences.base.Preference
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.model.user.User
import com.uragiristereo.mikansei.core.model.user.preference.DetailSizePreference
import com.uragiristereo.mikansei.core.model.user.preference.RatingPreference
import com.uragiristereo.mikansei.core.product.preference.BottomSheetPreferenceData
import com.uragiristereo.mikansei.core.resources.R
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class UserSettingsViewModel(
    private val userDao: UserDao,
    private val syncUserSettingsUseCase: SyncUserSettingsUseCase,
    private val updateUserSettingsUseCase: UpdateUserSettingsUseCase,
) : ViewModel() {
    var activeUser by mutableStateOf<User?>(null)
        private set

    var loading by mutableStateOf(true)
        private set

    var ratingFilters by mutableStateOf(
        value = BottomSheetPreferenceData(
            preferenceTextResId = R.string.settings_booru_listing_mode_select,
            items = RatingPreference.values().toList(),
            selectedItem = activeUser?.postsRatingFilter,
        ),
    )
        private set

    init {
        userDao.getActive()
            .onEach {
                activeUser = it.toUser()
                ratingFilters = ratingFilters.copy(selectedItem = it.postsRatingFilter)
            }
            .launchIn(viewModelScope)

        requestSync()
    }

    private fun requestSync() {
        viewModelScope.launch {
            syncUserSettingsUseCase().collect { result ->
                if (result is Result.Success) {
                    loading = false
                }
            }
        }
    }

    private fun updateSettings(data: UserField) {
        viewModelScope.launch {
            loading = activeUser?.id != 0

            updateUserSettingsUseCase(data).collect {
                requestSync()
            }
        }
    }

    fun onSafeModeChange(value: Boolean) {
        updateSettings(data = UserField(safeMode = value))
    }

    fun onShowDeletedPostsChange(value: Boolean) {
        updateSettings(data = UserField(showDeletedPosts = value))
    }

    fun onDetailSizeChange(value: DetailSizePreference) {
        updateSettings(data = UserField(defaultImageSize = value))
    }

    fun setBottomSheetPreferenceState(preference: Preference) {
        when (preference) {
            is RatingPreference -> {
                viewModelScope.launch {
                    activeUser?.let { user ->
                        userDao.update(
                            user.toUserRow().copy(postsRatingFilter = preference)
                        )
                    }
                }
            }
        }
    }
}
