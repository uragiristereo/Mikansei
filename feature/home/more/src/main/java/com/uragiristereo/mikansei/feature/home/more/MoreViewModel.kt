package com.uragiristereo.mikansei.feature.home.more

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Profile
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber

class MoreViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    val activeUser: StateFlow<Profile>
        get() = userRepository.active

    val isOnlyAnonUserExist = userRepository.getAll()
        .map {
            it.size == 1
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 2_000L),
            initialValue = true,
        )

    init {
        Timber.d(userRepository.toString())
    }
}
