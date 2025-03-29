package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.FavoriteResult
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.model.result.mapSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class GetEssentialFavoriteGroupsUseCase(
    private val danbooruRepository: DanbooruRepository,
    private val userRepository: UserRepository,
) {
    operator fun invoke(): Flow<Result<FavoriteResult>> {
        return flow {
            val activeUser = userRepository.active.value

            Timber.d("loading from cache...")
            val cachedResult = danbooruRepository.getFavoriteGroups(
                creatorId = activeUser.id,
                forceRefresh = false,
                forceLoadFromCache = true,
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
            val networkResult = danbooruRepository.getFavoriteGroups(
                creatorId = activeUser.id,
                forceRefresh = true,
                forceLoadFromCache = false,
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
