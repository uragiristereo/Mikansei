package com.uragiristereo.mejiboard.domain.usecase

import com.uragiristereo.mejiboard.core.booru.BooruRepository
import com.uragiristereo.mejiboard.core.common.data.Constants
import com.uragiristereo.mejiboard.core.database.dao.filters.FiltersDao
import com.uragiristereo.mejiboard.domain.entity.booru.tag.Tag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.concurrent.CancellationException

class SearchTermUseCase(
    private val booruRepository: BooruRepository,
    private val filtersDao: FiltersDao,
) {
    suspend operator fun invoke(
        term: String,
        onLoading: (loading: Boolean) -> Unit,
        onSuccess: (tags: List<Tag>) -> Unit,
        onFailed: (message: String) -> Unit,
        onError: (t: Throwable) -> Unit,
    ) {
        try {
            onLoading(true)

            delay(Constants.API_DELAY)

            val result = booruRepository.searchTerm(term)

            when (result.errorMessage) {
                null -> {
                    lateinit var filters: List<String>

                    withContext(Dispatchers.IO) {
                        filters = filtersDao.getEnabledFilters()
                    }

                    val filtered = result.data.filter { tag ->
                        !filters.any { filterTag ->
                            tag.name.lowercase()
                                .contains(other = filterTag.lowercase())
                        }
                    }.sortedByDescending { it.count }

                    onSuccess(filtered)
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
