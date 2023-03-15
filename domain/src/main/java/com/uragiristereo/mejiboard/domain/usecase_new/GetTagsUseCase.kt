package com.uragiristereo.mejiboard.domain.usecase_new

import com.uragiristereo.mejiboard.core.preferences.PreferencesRepository
import com.uragiristereo.mejiboard.domain.entity.tag.Tag
import com.uragiristereo.mejiboard.domain.entity.tag.toTag
import com.uragiristereo.mikansei.core.danbooru.repository.DanbooruRepository
import com.uragiristereo.mikansei.core.danbooru.result.Result
import com.uragiristereo.mikansei.core.danbooru.result.mapSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class GetTagsUseCase(
    private val danbooruRepository: DanbooruRepository,
    private val preferencesRepository: PreferencesRepository,
) {
    suspend operator fun invoke(tags: List<String>): Flow<Result<List<Tag>>> {
        val filters = preferencesRepository.flowData
            .first()
            .blacklistedTags

        return danbooruRepository.getTags(tags)
            .mapSuccess { danbooruTags ->
                danbooruTags.map { danbooruTag ->
                    val tag = danbooruTag.toTag()

                    tag.copy(isBlacklisted = tag.name in filters)
                }
            }
    }
}
