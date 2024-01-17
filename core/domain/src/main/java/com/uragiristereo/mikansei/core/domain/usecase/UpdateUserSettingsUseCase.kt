package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.ProfileSettingsField
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.model.result.Result

class UpdateUserSettingsUseCase(
    private val danbooruRepository: DanbooruRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(data: ProfileSettingsField): Result<Unit> {
        val activeUser = userRepository.active.value

        return when {
            activeUser.isAnonymous() -> {
                val changes = activeUser.copy(
                    danbooru = activeUser.danbooru.copy(
                        safeMode = data.enableSafeMode ?: activeUser.danbooru.safeMode,
                        showDeletedPosts = data.showDeletedPosts ?: activeUser.danbooru.showDeletedPosts,
                        defaultImageSize = data.defaultImageSize ?: activeUser.danbooru.defaultImageSize,
                        blacklistedTags = data.blacklistedTags ?: activeUser.danbooru.blacklistedTags,
                    ),
                )

                userRepository.update(changes)

                Result.Success(Unit)
            }

            else -> danbooruRepository.updateUserSettings(
                id = activeUser.id,
                field = data,
            )
        }
    }
}
