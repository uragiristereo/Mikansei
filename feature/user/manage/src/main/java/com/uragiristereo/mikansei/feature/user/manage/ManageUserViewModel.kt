package com.uragiristereo.mikansei.feature.user.manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Profile
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.feature.user.manage.core.UsersState
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ManageUserViewModel(
    private val danbooruRepository: DanbooruRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    val usersState = userRepository.getAll()
        .map { users ->
            val activeUser = users.first { it.mikansei.isActive }
            val inactiveUsers = users.filter { !it.mikansei.isActive }

            UsersState(activeUser, inactiveUsers)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 2_000L),
            initialValue = UsersState(
                active = userRepository.active.value,
                inactive = emptyList(),
            ),
        )

    val allUsers = userRepository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 2_000L),
            initialValue = emptyList(),
        )

    fun switchActiveUser(user: Profile) {
        viewModelScope.launch {
            launch(SupervisorJob()) {
                danbooruRepository.removeCachedEndpoints()
            }

            userRepository.switchActive(user.id)
        }
    }

    fun logoutUser(user: Profile) {
        viewModelScope.launch {
            userRepository.delete(user)
        }
    }
}
