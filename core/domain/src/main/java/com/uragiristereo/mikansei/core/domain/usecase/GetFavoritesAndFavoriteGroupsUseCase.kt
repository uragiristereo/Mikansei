package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.domain.entity.favorite.Favorite
import com.uragiristereo.mikansei.core.model.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetFavoritesAndFavoriteGroupsUseCase(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val getFavoriteGroupsUseCase: GetFavoriteGroupsUseCase,
) {
    suspend operator fun invoke(): Flow<Result<List<Favorite>>> {
        return getFavoritesUseCase().combine(getFavoriteGroupsUseCase()) { result1, result2 ->
            when {
                result1 is Result.Success && result2 is Result.Success -> {
                    Result.Success(
                        data = listOf(result1.data) + result2.data
                    )
                }

                result1 is Result.Failed -> Result.Failed(result1.message)
                result2 is Result.Failed -> Result.Failed(result2.message)
                result1 is Result.Error -> Result.Error(result1.t)
                result2 is Result.Error -> Result.Error(result2.t)

                else -> Result.Error(IllegalStateException("Unexpected result types"))
            }
        }
    }
}
