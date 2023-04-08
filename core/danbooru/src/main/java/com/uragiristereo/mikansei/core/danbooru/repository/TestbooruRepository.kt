package com.uragiristereo.mikansei.core.danbooru.repository

import android.content.Context
import com.uragiristereo.mikansei.core.database.dao.user.UserDao
import com.uragiristereo.mikansei.core.model.danbooru.DanbooruHost
import com.uragiristereo.mikansei.core.network.NetworkRepository

class TestbooruRepository(
    context: Context,
    networkRepository: NetworkRepository,
    userDao: UserDao,
) : DanbooruRepositoryImpl(context, networkRepository, userDao) {
    override val host = DanbooruHost.Testbooru
    override val safeHost = DanbooruHost.Testbooru
}
