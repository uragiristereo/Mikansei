package com.uragiristereo.mikansei.core.danbooru.repository

import com.uragiristereo.mikansei.core.database.dao.user.UserDao
import com.uragiristereo.mikansei.core.model.danbooru.DanbooruHost
import com.uragiristereo.mikansei.core.network.NetworkRepository

class TestbooruRepository(
    networkRepository: NetworkRepository,
    userDao: UserDao,
) : DanbooruRepositoryImpl(networkRepository, userDao) {
    override val host = DanbooruHost.Testbooru
}
