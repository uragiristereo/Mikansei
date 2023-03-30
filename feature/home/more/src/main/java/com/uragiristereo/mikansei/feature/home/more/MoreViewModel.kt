package com.uragiristereo.mikansei.feature.home.more

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uragiristereo.mikansei.core.database.dao.user.UserDao
import com.uragiristereo.mikansei.core.database.dao.user.toUser
import com.uragiristereo.mikansei.core.model.user.User
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MoreViewModel(
    userDao: UserDao,
) : ViewModel() {
    var activeUser by mutableStateOf<User?>(null)
        private set

    init {
        userDao.getActive()
            .onEach { activeUser = it.toUser() }
            .launchIn(viewModelScope)
    }
}
