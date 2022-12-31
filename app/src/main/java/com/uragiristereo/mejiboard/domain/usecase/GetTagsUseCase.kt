package com.uragiristereo.mejiboard.domain.usecase

import com.uragiristereo.mejiboard.domain.entity.source.tag.Tag
import com.uragiristereo.mejiboard.domain.repository.BoorusRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import kotlin.coroutines.cancellation.CancellationException

class GetTagsUseCase : KoinComponent {
    private val boorusRepository: BoorusRepository = get()

    suspend operator fun invoke(
        tags: List<String>,
        onLoading: (loading: Boolean) -> Unit,
        onSuccess: (tags: List<Tag>) -> Unit,
        onFailed: (message: String) -> Unit,
        onError: (t: Throwable) -> Unit,
    ) {
        try {
            onLoading(true)

            val result = boorusRepository.getTags(tags = tags)

            when (result.errorMessage) {
                null -> {
                    val sorted = result.data.sortedBy { it.name }

                    onSuccess(sorted)
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
