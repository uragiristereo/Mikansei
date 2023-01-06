package com.uragiristereo.mejiboard.domain.usecase

import com.uragiristereo.mejiboard.core.booru.BooruRepository
import com.uragiristereo.mejiboard.core.model.booru.tag.Tag
import kotlin.coroutines.cancellation.CancellationException

class GetTagsUseCase(
    private val booruRepository: BooruRepository,
) {
    suspend operator fun invoke(
        tags: List<String>,
        onLoading: (loading: Boolean) -> Unit,
        onSuccess: (tags: List<Tag>) -> Unit,
        onFailed: (message: String) -> Unit,
        onError: (t: Throwable) -> Unit,
    ) {
        try {
            onLoading(true)

            val result = booruRepository.getTags(tags = tags)

            when (result.errorMessage) {
                null -> {
                    val sorted = result.data.sortedBy { it.name }

                    onSuccess(sorted)
                }

                else -> onFailed(result.errorMessage.toString())
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
