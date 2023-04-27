package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.domain.entity.favorite.Favorite
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.model.result.combineSuccess
import kotlinx.coroutines.flow.Flow

class GetFavoritesAndFavoriteGroupsUseCase(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val getFavoriteGroupsUseCase: GetFavoriteGroupsUseCase,
) {
    suspend operator fun invoke(): Flow<Result<List<Favorite>>> {
        return getFavoritesUseCase()
            .combineSuccess(
                resultFlow = getFavoriteGroupsUseCase(
                    forceCache = false,
                    forceRefresh = true,
                ),
                transform = { result1, result2 ->
                    listOf(result1) + result2
                },
            )
    }
}
