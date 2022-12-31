package com.uragiristereo.mejiboard.domain.usecase

import com.uragiristereo.mejiboard.common.Constants
import com.uragiristereo.mejiboard.data.database.DatabaseRepository
import com.uragiristereo.mejiboard.domain.entity.source.tag.Tag
import com.uragiristereo.mejiboard.domain.repository.BoorusRepository
import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.util.concurrent.CancellationException

class SearchTermUseCase : KoinComponent {
    private val boorusRepository: BoorusRepository = get()
    private val databaseRepository: DatabaseRepository = get()

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
                    val filters = databaseRepository.filtersDao().getEnabledFilters()

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
