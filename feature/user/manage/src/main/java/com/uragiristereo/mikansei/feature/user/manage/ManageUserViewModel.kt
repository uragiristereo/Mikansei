package com.uragiristereo.mikansei.feature.user.manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Profile
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ManageUserViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    val activeUser: StateFlow<Profile>
        get() = userRepository.active

    val inactiveUsers = userRepository.getAll()
        .map { profileList ->
            profileList.filter {
                !it.mikansei.isActive
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 2_000L),
            initialValue = emptyList(),
        )

    val allUsers = userRepository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 2_000L),
            initialValue = emptyList(),
        )

    fun switchActiveUser(user: Profile) {
        viewModelScope.launch {
            userRepository.switchActive(user.id)
        }
    }

    fun logoutUser(user: Profile) {
        viewModelScope.launch {
            userRepository.delete(user)
        }
    }
}
