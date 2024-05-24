package com.uragiristereo.mikansei.feature.user.delegation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uragiristereo.mikansei.core.domain.module.database.UserDelegationRepository
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.feature.user.delegation.list.UserDelegation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class UserDelegationSettingsViewModel(
    private val userRepository: UserRepository,
    private val userDelegationRepository: UserDelegationRepository,
) : ViewModel() {
    val activeUser = userRepository.active

    private val activeUserId: Int
        get() = userRepository.active.value.id

    private val delegatedUserIdFlow = userDelegationRepository
        .getDelegatedUserById(activeUserId)
        .map { delegatedUserId ->
            if (activeUserId != delegatedUserId) {
                delegatedUserId
            } else {
                null
            }
        }

    val users = delegatedUserIdFlow
        .flatMapLatest { delegatedUserId ->
            userRepository.getAll()
                .map { users ->
                    val currentUser = UserDelegation(
                        userId = activeUserId,
                        selected = delegatedUserId == null,
                    )

                    val mappedUsers = users.filter { user ->
                        user.id != activeUserId && user.isNotAnonymous()
                    }.map { user ->
                        val selected = when {
                            delegatedUserId != null && user.id == delegatedUserId -> true
                            delegatedUserId == null && user.id == activeUserId -> true
                            else -> false
                        }

                        UserDelegation(
                            userId = user.id,
                            name = user.name,
                            selected = selected,
                        )
                    }

                    listOf(currentUser) + mappedUsers
                }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000L),
            initialValue = emptyList(),
        )

    fun setDelegation(targetUserId: Int) {
        viewModelScope.launch(SupervisorJob()) {
            userDelegationRepository.setDelegation(
                userId = activeUserId,
                delegatedUserId = targetUserId,
            )
        }
    }
}
