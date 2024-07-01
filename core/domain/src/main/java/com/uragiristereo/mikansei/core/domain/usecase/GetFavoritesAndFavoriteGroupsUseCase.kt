package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Favorite
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.model.result.combineSuccess

class GetFavoritesAndFavoriteGroupsUseCase(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val getFavoriteGroupsUseCase: GetFavoriteGroupsUseCase,
) {
    suspend operator fun invoke(): Result<List<Favorite>> {
        return combineSuccess(
            first = {
                getFavoritesUseCase()
            },
            other = {
                getFavoriteGroupsUseCase(forceRefresh = true)
            },
            transform = { result1, result2 ->
                listOf(result1) + result2
            },
        )
    }
}
