package com.uragiristereo.mikansei.feature.home.more

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uragiristereo.mikansei.core.database.dao.user.UserDao
import com.uragiristereo.mikansei.core.domain.module.database.model.toProfile
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MoreViewModel(
    userDao: UserDao,
) : ViewModel() {
    val activeUser = userDao.getActive()
        .map {
            it.toProfile()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 2_000L),
            initialValue = null,
        )

    val isOnlyAnonUserExist = userDao.getAll()
        .map {
            it.size == 1
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 2_000L),
            initialValue = true,
        )
}
