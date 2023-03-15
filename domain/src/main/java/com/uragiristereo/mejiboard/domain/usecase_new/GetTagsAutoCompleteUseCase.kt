package com.uragiristereo.mejiboard.domain.usecase_new

import com.uragiristereo.mejiboard.core.model.result.Result
import com.uragiristereo.mejiboard.core.model.result.mapSuccess
import com.uragiristereo.mejiboard.core.preferences.PreferencesRepository
import com.uragiristereo.mejiboard.domain.entity.tag.TagAutoComplete
import com.uragiristereo.mejiboard.domain.entity.tag.toTagAutoCompleteList
import com.uragiristereo.mikansei.core.danbooru.repository.DanbooruRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class GetTagsAutoCompleteUseCase(
    private val danbooruRepository: DanbooruRepository,
    private val preferencesRepository: PreferencesRepository,
) {
    suspend operator fun invoke(query: String): Flow<Result<List<TagAutoComplete>>> {
        return danbooruRepository.getTagsAutoComplete(query)
            .mapSuccess { danbooruTags ->
                val filters = preferencesRepository.flowData
                    .first()
                    .blacklistedTags

                val tags = danbooruTags.toTagAutoCompleteList()

                val filtered = when {
                    filters.isNotEmpty() -> tags
                        .filter { item ->
                            item.value !in filters
                        }

                    else -> tags
                }

                filtered
            }
    }
}
