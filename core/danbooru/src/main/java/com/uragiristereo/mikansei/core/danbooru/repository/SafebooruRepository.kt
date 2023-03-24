package com.uragiristereo.mikansei.core.danbooru.repository

import com.uragiristereo.mikansei.core.model.danbooru.DanbooruHost
import com.uragiristereo.mikansei.core.network.NetworkRepository

class SafebooruRepository(
    networkRepository: NetworkRepository,
) : DanbooruRepositoryImpl(networkRepository) {
    override val host = DanbooruHost.Safebooru
}
