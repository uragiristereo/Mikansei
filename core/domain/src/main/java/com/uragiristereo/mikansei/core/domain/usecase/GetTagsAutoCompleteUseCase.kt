package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.AutoComplete
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.model.preferences.user.RatingPreference
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.model.result.mapSuccess

class GetTagsAutoCompleteUseCase(
    private val danbooruRepository: DanbooruRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(
        query: String,
        searchType: AutoComplete.SearchType,
    ): Result<List<AutoComplete>> {
        return danbooruRepository.getAutoComplete(query, searchType)
            .mapSuccess { items ->
                val activeUser = userRepository.active.value
                val filters = activeUser.danbooru.blacklistedTags

                val safeRatings = listOf(
                    RatingPreference.GENERAL_ONLY,
                    RatingPreference.SAFE,
                )

                val unsafeTags = when {
                    activeUser.danbooru.safeMode || activeUser.mikansei.postsRatingFilter in safeRatings -> danbooruRepository.unsafeTags
                    else -> listOf()
                }

                val filtered = items.filter { item ->
                    item.value !in filters && item.value !in unsafeTags
                }

                filtered
            }
    }
}
