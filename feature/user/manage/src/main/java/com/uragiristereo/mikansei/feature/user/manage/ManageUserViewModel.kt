package com.uragiristereo.mikansei.feature.user.manage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uragiristereo.mikansei.core.database.dao.user.UserDao
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Profile
import com.uragiristereo.mikansei.core.domain.module.database.model.toProfile
import com.uragiristereo.mikansei.core.domain.module.database.model.toProfileList
import com.uragiristereo.mikansei.core.domain.module.database.model.toUserRow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ManageUserViewModel(
    private val userDao: UserDao,
) : ViewModel() {
    var activeUser by mutableStateOf<Profile?>(null)
        private set

    var inactiveUsers by mutableStateOf(emptyList<Profile>())
        private set

    init {
        userDao.getActive()
            .onEach { activeUser = it.toProfile() }
            .launchIn(viewModelScope)

        userDao.getInactive()
            .onEach { inactiveUsers = it.toProfileList() }
            .launchIn(viewModelScope)
    }

    fun switchActiveUser(user: Profile) {
        viewModelScope.launch {
            userDao.switchActiveUser(user.id)
        }
    }

    fun logoutUser(user: Profile) {
        viewModelScope.launch {
            userDao.delete(user.toUserRow())
        }
    }
}
