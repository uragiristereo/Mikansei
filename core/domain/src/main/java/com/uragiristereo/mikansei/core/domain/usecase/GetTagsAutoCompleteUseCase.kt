package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.database.dao.user.UserDao
import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Tag
import com.uragiristereo.mikansei.core.domain.module.database.model.toProfile
import com.uragiristereo.mikansei.core.model.preferences.user.RatingPreference
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.model.result.mapSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class GetTagsAutoCompleteUseCase(
    private val danbooruRepository: DanbooruRepository,
    private val userDao: UserDao,
) {
    suspend operator fun invoke(query: String): Flow<Result<List<Tag>>> {
        return danbooruRepository.getTagsAutoComplete(query)
            .mapSuccess { tags ->
                val activeUser = userDao.getActive()
                    .first()
                    .toProfile()

                val filters = activeUser.danbooru.blacklistedTags

                val safeRatings = listOf(
                    RatingPreference.GENERAL_ONLY,
                    RatingPreference.SAFE,
                )

                val unsafeTags = when {
                    activeUser.danbooru.safeMode || activeUser.mikansei!!.postsRatingFilter in safeRatings -> danbooruRepository.unsafeTags
                    else -> listOf()
                }

                val filtered = tags.filter { item ->
                    item.name !in filters && item.name !in unsafeTags
                }

                filtered
            }
    }
}
