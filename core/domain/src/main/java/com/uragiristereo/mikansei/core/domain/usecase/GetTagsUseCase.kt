package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.database.dao.user.UserDao
import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Tag
import com.uragiristereo.mikansei.core.domain.module.database.model.toProfile
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.model.result.mapSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class GetTagsUseCase(
    private val danbooruRepository: DanbooruRepository,
    private val userDao: UserDao,
) {
    suspend operator fun invoke(tags: List<String>): Flow<Result<List<Tag>>> {
        val filters = userDao.getActive()
            .first()
            .toProfile()
            .danbooru
            .blacklistedTags

        return danbooruRepository.getTags(tags)
            .mapSuccess { danbooruTags ->
                val sortedTags = danbooruTags.map { tag ->
                    tag.copy(isBlacklisted = tag.name in filters)
                }.sortedBy { it.name }

                val artistTags = mutableListOf<Tag>()
                val copyrightTags = mutableListOf<Tag>()
                val characterTags = mutableListOf<Tag>()
                val metaTags = mutableListOf<Tag>()
                val generalTags = mutableListOf<Tag>()

                sortedTags.forEach { tag ->
                    when (tag.category) {
                        Tag.Category.ARTIST -> artistTags.add(tag)
                        Tag.Category.COPYRIGHT -> copyrightTags.add(tag)
                        Tag.Category.CHARACTER -> characterTags.add(tag)
                        Tag.Category.META -> metaTags.add(tag)
                        else -> generalTags.add(tag)
                    }
                }

                val arrangedTags = artistTags + copyrightTags + characterTags + metaTags + generalTags

                arrangedTags
            }
    }
}
