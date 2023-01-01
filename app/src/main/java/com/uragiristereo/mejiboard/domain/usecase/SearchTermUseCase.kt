package com.uragiristereo.mejiboard.domain.usecase

import com.uragiristereo.mejiboard.common.Constants
import com.uragiristereo.mejiboard.data.database.filters.FiltersDao
import com.uragiristereo.mejiboard.domain.entity.source.tag.Tag
import com.uragiristereo.mejiboard.domain.repository.BoorusRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.concurrent.CancellationException

class SearchTermUseCase(
    private val boorusRepository: BoorusRepository,
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

            val result = boorusRepository.searchTerm(term)

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
