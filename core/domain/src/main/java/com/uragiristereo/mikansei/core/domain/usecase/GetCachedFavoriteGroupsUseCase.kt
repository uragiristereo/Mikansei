package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.FavoriteResult
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.model.result.mapSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class GetCachedFavoriteGroupsUseCase(
    private val getFavoriteGroupsUseCase: GetFavoriteGroupsUseCase,
) {
    operator fun invoke(): Flow<Result<FavoriteResult>> {
        return flow {
            Timber.d("loading from cache...")
            val cachedResult = getFavoriteGroupsUseCase.invoke(
                forceRefresh = false,
                forceLoadFromCache = true,
                shouldLoadThumbnails = false,
            ).mapSuccess { items ->
                FavoriteResult(
                    isFromCache = true,
                    items = items,
                )
            }

            // TODO: properly handle response codes
            val hasNoCachedData = cachedResult is Result.Failed && cachedResult.message.startsWith("504:")
            Timber.d("hasNoCachedData=$hasNoCachedData")

            if (!hasNoCachedData) {
                Timber.d("cached result $cachedResult")
                emit(cachedResult)
            }

            Timber.d("loading from network...")
            val networkResult = getFavoriteGroupsUseCase.invoke(
                forceRefresh = true,
                forceLoadFromCache = false,
                shouldLoadThumbnails = false,
            ).mapSuccess { items ->
                FavoriteResult(
                    isFromCache = false,
                    items = items,
                )
            }

            Timber.d("network result $networkResult")
            emit(networkResult)
        }
    }
}
