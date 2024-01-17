package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.model.result.Result
import kotlinx.coroutines.flow.first

class SyncUserSettingsUseCase(
    private val danbooruRepository: DanbooruRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(): Result<Unit> {
        val activeUser = userRepository.active.value

        return when {
            activeUser.isAnonymous() -> return Result.Success(Unit)

            else -> {
                when (val result = danbooruRepository.getProfile()) {
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
