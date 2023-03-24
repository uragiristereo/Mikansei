package com.uragiristereo.mikansei.core.danbooru.repository

import com.uragiristereo.mikansei.core.model.danbooru.DanbooruHost
import com.uragiristereo.mikansei.core.network.NetworkRepository

class TestbooruRepository(
    networkRepository: NetworkRepository,
) : DanbooruRepositoryImpl(networkRepository) {
    override val host = DanbooruHost.Testbooru
}
