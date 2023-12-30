package com.uragiristereo.mikansei.feature.home.favorites.favgroup.more

import androidx.lifecycle.ViewModel
import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository

class FavGroupMoreViewModel(
    private val danbooruRepository: DanbooruRepository,
) : ViewModel() {
    fun getBaseUrl() = danbooruRepository.host.getBaseUrl()
}
