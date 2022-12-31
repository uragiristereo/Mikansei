package com.uragiristereo.mejiboard.data.source

import com.uragiristereo.mejiboard.data.source.danbooru.DanbooruRepository
import com.uragiristereo.mejiboard.data.source.gelbooru.GelbooruRepository
import com.uragiristereo.mejiboard.data.source.safebooruorg.SafebooruOrgRepository
import com.uragiristereo.mejiboard.domain.entity.source.BooruSource
import com.uragiristereo.mejiboard.domain.entity.source.post.PostsResult
import com.uragiristereo.mejiboard.domain.entity.source.tag.TagsResult
import com.uragiristereo.mejiboard.domain.repository.BooruSourceRepository
import com.uragiristereo.mejiboard.domain.repository.BoorusRepository
import com.uragiristereo.mejiboard.domain.repository.NetworkRepository
import com.uragiristereo.mejiboard.domain.repository.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class BoorusRepositoryImpl : BoorusRepository, KoinComponent {
    private val networkRepository: NetworkRepository = get()
    private val preferencesRepository: PreferencesRepository = get()

    private var preferences = preferencesRepository.data

    private var dohEnabled = preferences.dohEnabled

    private val preferredOkHttpClient: OkHttpClient
        get() = networkRepository.getPreferredOkHttpClient(doh = dohEnabled)

    private val ratingFilters
        get() = preferences.ratingFilters

    override val currentBooru
        get() = BooruSources.getBooruByKey(preferences.booru) ?: BooruSources.Gelbooru

    override var boorus: Map<BooruSource, BooruSourceRepository> = initializeBoorus()

    private val currentBooruRepository: BooruSourceRepository
        get() = boorus[currentBooru]!!

    init {
        CoroutineScope(Dispatchers.Main).launch {
            preferencesRepository.flowData.collect {
                preferences = it

                if (dohEnabled != preferences.dohEnabled) {
                    dohEnabled = preferences.dohEnabled

                    initializeBoorus()
                }
            }
        }
    }

    private fun initializeBoorus(): Map<BooruSource, BooruSourceRepository> {
        return mapOf(
            BooruSources.Gelbooru to GelbooruRepository(preferredOkHttpClient),
            BooruSources.Danbooru to DanbooruRepository(preferredOkHttpClient),
            BooruSources.SafebooruOrg to SafebooruOrgRepository(preferredOkHttpClient),
        )
    }

    override suspend fun getPosts(tags: String, page: Int): PostsResult {
        return currentBooruRepository.getPosts(tags, page, ratingFilters)
    }

    override suspend fun searchTerm(term: String): TagsResult {
        return currentBooruRepository.searchTerm(term, ratingFilters)
    }

    override suspend fun getTags(tags: List<String>): TagsResult {
        return currentBooruRepository.getTags(tags)
    }
}
