package com.uragiristereo.mejiboard.domain.usecase

import com.uragiristereo.mejiboard.data.database.DatabaseRepository
import com.uragiristereo.mejiboard.data.preferences.Preferences
import com.uragiristereo.mejiboard.domain.entity.source.BooruSource
import com.uragiristereo.mejiboard.domain.entity.source.post.Post
import com.uragiristereo.mejiboard.domain.repository.BoorusRepository
import com.uragiristereo.mejiboard.domain.repository.PreferencesRepository
import kotlinx.coroutines.CancellationException
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class GetPostsUseCase : KoinComponent {
    private val boorusRepository: BoorusRepository = get()
    private val preferencesRepository: PreferencesRepository = get()
    private val databaseRepository: DatabaseRepository = get()

    private val preferences: Preferences
        get() = preferencesRepository.data

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

            onStart(boorusRepository.currentBooru)

            val result = boorusRepository.getPosts(
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

                    var filtered = result.data

                    if (preferences.filtersEnabled) {
                        // filter by tags
                        val filters = databaseRepository.filtersDao().getEnabledFilters()

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
