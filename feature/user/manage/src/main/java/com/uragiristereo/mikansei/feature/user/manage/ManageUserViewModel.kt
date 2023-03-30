package com.uragiristereo.mikansei.feature.user.manage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uragiristereo.mikansei.core.database.dao.user.UserDao
import com.uragiristereo.mikansei.core.database.dao.user.toUser
import com.uragiristereo.mikansei.core.database.dao.user.toUserList
import com.uragiristereo.mikansei.core.database.dao.user.toUserRow
import com.uragiristereo.mikansei.core.model.user.User
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ManageUserViewModel(
    private val userDao: UserDao,
) : ViewModel() {
    var activeUser by mutableStateOf<User?>(null)
        private set

    var inactiveUsers by mutableStateOf(emptyList<User>())
        private set

    init {
        userDao.getActive()
            .onEach { activeUser = it.toUser() }
            .launchIn(viewModelScope)

        userDao.getInactive()
            .onEach { inactiveUsers = it.toUserList() }
            .launchIn(viewModelScope)
    }

    fun switchActiveUser(user: User) {
        viewModelScope.launch {
            userDao.switchActiveUser(user.id)
        }
    }

    fun logoutUser(user: User) {
        viewModelScope.launch {
            userDao.delete(user.toUserRow())
        }
    }
}
