package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.danbooru.repository.DanbooruRepository
import com.uragiristereo.mikansei.core.database.dao.user.UserDao
import com.uragiristereo.mikansei.core.database.dao.user.toUser
import com.uragiristereo.mikansei.core.domain.entity.tag.Tag
import com.uragiristereo.mikansei.core.domain.entity.tag.TagCategory
import com.uragiristereo.mikansei.core.domain.entity.tag.toTag
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
            .toUser()
            .blacklistedTags

        return danbooruRepository.getTags(tags)
            .mapSuccess { danbooruTags ->
                val sortedTags = danbooruTags.map { danbooruTag ->
                    val tag = danbooruTag.toTag()

                    tag.copy(isBlacklisted = tag.name in filters)
                }.sortedBy { it.name }

                val artistTags = mutableListOf<Tag>()
                val copyrightTags = mutableListOf<Tag>()
                val characterTags = mutableListOf<Tag>()
                val metaTags = mutableListOf<Tag>()
                val generalTags = mutableListOf<Tag>()

                sortedTags.forEach { tag ->
                    when (tag.category) {
                        TagCategory.ARTIST -> artistTags.add(tag)
                        TagCategory.COPYRIGHT -> copyrightTags.add(tag)
                        TagCategory.CHARACTER -> characterTags.add(tag)
                        TagCategory.META -> metaTags.add(tag)
                        else -> generalTags.add(tag)
                    }
                }

                val arrangedTags = artistTags + copyrightTags + characterTags + metaTags + generalTags

                arrangedTags
            }
    }
}
