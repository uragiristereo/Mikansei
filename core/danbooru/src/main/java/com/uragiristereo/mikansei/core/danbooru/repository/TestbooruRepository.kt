package com.uragiristereo.mikansei.core.danbooru.repository

import com.uragiristereo.mejiboard.core.network.NetworkRepository
import com.uragiristereo.mejiboard.core.preferences.model.DanbooruSubdomain

class TestbooruRepository(
    networkRepository: NetworkRepository,
) : DanbooruRepositoryImpl(networkRepository) {
    override val subdomain = DanbooruSubdomain.TESTBOORU
}
