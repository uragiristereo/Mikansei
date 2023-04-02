package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.danbooru.repository.DanbooruRepository
import com.uragiristereo.mikansei.core.database.dao.user.UserDao
import com.uragiristereo.mikansei.core.database.dao.user.toUser
import com.uragiristereo.mikansei.core.domain.entity.tag.TagAutoComplete
import com.uragiristereo.mikansei.core.domain.entity.tag.toTagAutoCompleteList
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.model.result.mapSuccess
import com.uragiristereo.mikansei.core.model.user.preference.RatingPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class GetTagsAutoCompleteUseCase(
    private val danbooruRepository: DanbooruRepository,
    private val userDao: UserDao,
) {
    suspend operator fun invoke(query: String): Flow<Result<List<TagAutoComplete>>> {
        return danbooruRepository.getTagsAutoComplete(query)
            .mapSuccess { danbooruTags ->
                val tags = danbooruTags.toTagAutoCompleteList()

                val activeUser = userDao.getActive()
                    .first()
                    .toUser()

                val filters = activeUser.blacklistedTags

                val safeRatings = listOf(
                    RatingPreference.GENERAL_ONLY,
                    RatingPreference.SAFE,
                )

                val unsafeTags = when {
                    activeUser.safeMode || activeUser.postsRatingFilter in safeRatings -> danbooruRepository.unsafeTags
                    else -> listOf()
                }

                val filtered = tags.filter { item ->
                    item.value !in filters && item.value !in unsafeTags
                }

                filtered
            }
    }
}
