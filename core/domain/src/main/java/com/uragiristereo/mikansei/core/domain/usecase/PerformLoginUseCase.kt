package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.model.Environment
import com.uragiristereo.mikansei.core.model.result.Result

class PerformLoginUseCase(
    private val danbooruRepository: DanbooruRepository,
    private val userRepository: UserRepository,
    private val environment: Environment,
) {
    suspend operator fun invoke(
        name: String,
        apiKey: String,
    ): Result<Boolean> {
        return when (val result = danbooruRepository.login(name, apiKey)) {
            is Result.Success -> {
                val profile = result.data
                val userExists = userRepository.isUserExists(profile.id)

                when {
                    profile.name != name -> Result.Failed(message = "Invalid name/API key.")
                    userExists -> Result.Failed("User is already logged in.")
                    else -> {
                        userRepository.add(
                            user = profile.copy(
                                apiKey = apiKey,
                                mikansei = profile.mikansei.copy(showPendingPosts = !environment.safeMode),
                            )
                        )

                        userRepository.switchActive(profile.id)

                        Result.Success(true)
                    }
                }
            }

            is Result.Failed -> Result.Failed(result.message)
            is Result.Error -> Result.Error(result.t)
        }
    }
}
