package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.model.result.Result

class DeactivateAccountUseCase(
    private val danbooruRepository: DanbooruRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(password: String): Result<Unit> {
        val activeUser = userRepository.active.value
        val result = danbooruRepository.deactivateAccount(activeUser.id, password)

        if (result is Result.Success) {
            userRepository.switchToAnonymousAndDelete(activeUser)
        }

        return result
    }
}
