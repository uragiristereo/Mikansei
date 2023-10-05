package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.model.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class SyncUserSettingsUseCase(
    private val danbooruRepository: DanbooruRepository,
    private val userRepository: UserRepository,
) {
    operator fun invoke(): Flow<Result<Unit>> {
        val activeUser = userRepository.active.value

        return when {
            activeUser.isAnonymous() -> flow {
                emit(Result.Success(Unit))
            }

            else -> danbooruRepository.getProfile()
                .map { result ->
                    when (result) {
                        is Result.Success -> {
                            val netProfile = result.data
                            val localProfile = userRepository.get(netProfile.id).first()

                            userRepository.update(
                                localProfile.copy(
                                    name = netProfile.name,
                                    level = netProfile.level,
                                    danbooru = netProfile.danbooru,
                                )
                            )

                            Result.Success(Unit)
                        }

                        is Result.Failed -> Result.Failed(result.message)
                        is Result.Error -> Result.Error(result.t)
                    }
                }
        }
    }
}
