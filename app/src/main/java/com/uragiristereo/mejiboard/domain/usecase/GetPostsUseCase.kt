package com.uragiristereo.mejiboard.domain.usecase

import com.uragiristereo.mejiboard.core.booru.BooruRepository
import com.uragiristereo.mejiboard.core.booru.source.BooruSource
import com.uragiristereo.mejiboard.core.database.dao.filters.FiltersDao
import com.uragiristereo.mejiboard.core.preferences.PreferencesRepository
import com.uragiristereo.mejiboard.domain.entity.booru.post.Post
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class GetPostsUseCase(
    private val booruRepository: BooruRepository,
    private val preferencesRepository: PreferencesRepository,
    private val filtersDao: FiltersDao,
) {
    suspend operator fun invoke(
        tags: String,
        page: Int,
        currentPosts: List<Post>,
        onStart: (BooruSource) -> Unit,
        onLoading: (loading: Boolean) -> Unit,
        onSuccess: (posts: List<Post>, canLoadMore: Boolean) -> Unit,
        onFailed: (message: String) -> Unit,
        onError: (t: Throwable) -> Unit,
    ) {
        try {
            onLoading(true)

            onStart(booruRepository.currentBooru)

            val result = booruRepository.getPosts(
                tags = tags,
                page = page,
            )

            when (result.errorMessage) {
                null -> {
                    // if page == 0 means it's refreshing and previous posts should be cleared
                    val previousPosts = when (page) {
                        0 -> listOf()
                        else -> currentPosts
                    }

                    val filtersEnabled = preferencesRepository.flowData.first().filtersEnabled
                    var filtered = result.data

                    if (filtersEnabled) {
                        // filter by tags
                        lateinit var filters: List<String>

                        withContext(Dispatchers.IO) {
                            filters = filtersDao.getEnabledFilters()
                        }

                        filtered = result.data.filter { post ->
                            val tagsAsList = post.tags
                                .lowercase()
                                .split(' ')

                            !filters.any { tag ->
                                tagsAsList.contains(tag.lowercase())
                            }
                        }
                    }

                    val canLoadMore = when {
                        result.canLoadMore && filtered.isEmpty() -> false
                        else -> result.canLoadMore
                    }

                    // remove duplicate posts
                    val combined = previousPosts.plus(filtered).distinctBy { it.id }

                    onSuccess(combined, canLoadMore)
                }

                else -> onFailed(result.errorMessage)
            }

            onLoading(false)
        } catch (t: Throwable) {
            when (t) {
                is CancellationException -> {}
                else -> onError(t)
            }

            onLoading(false)
        }
    }
}
