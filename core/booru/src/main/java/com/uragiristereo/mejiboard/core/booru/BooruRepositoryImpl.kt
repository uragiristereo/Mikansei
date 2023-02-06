package com.uragiristereo.mejiboard.core.booru

import android.content.Context
import com.uragiristereo.mejiboard.core.booru.source.BooruSourceRepository
import com.uragiristereo.mejiboard.core.booru.source.danbooru.DanbooruRepository
import com.uragiristereo.mejiboard.core.booru.source.gelbooru.GelbooruRepository
import com.uragiristereo.mejiboard.core.booru.source.safebooruorg.SafebooruOrgRepository
import com.uragiristereo.mejiboard.core.booru.source.yandere.YandereRepository
import com.uragiristereo.mejiboard.core.model.booru.BooruSource
import com.uragiristereo.mejiboard.core.model.booru.BooruSources
import com.uragiristereo.mejiboard.core.model.booru.post.PostsResult
import com.uragiristereo.mejiboard.core.model.booru.tag.TagsResult
import com.uragiristereo.mejiboard.core.network.NetworkRepository
import com.uragiristereo.mejiboard.core.network.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

class BooruRepositoryImpl(
    private val context: Context,
    private val networkRepository: NetworkRepository,
    private val preferencesRepository: PreferencesRepository,
) : BooruRepository {
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
            BooruSources.Gelbooru to GelbooruRepository(context, preferredOkHttpClient),
            BooruSources.Danbooru to DanbooruRepository(context, preferredOkHttpClient),
            BooruSources.SafebooruOrg to SafebooruOrgRepository(context, preferredOkHttpClient),
            BooruSources.Yandere to YandereRepository(context, preferredOkHttpClient),
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
