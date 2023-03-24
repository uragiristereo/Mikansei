package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.danbooru.repository.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.entity.tag.TagAutoComplete
import com.uragiristereo.mikansei.core.domain.entity.tag.toTagAutoCompleteList
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.model.result.mapSuccess
import com.uragiristereo.mikansei.core.preferences.PreferencesRepository
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
