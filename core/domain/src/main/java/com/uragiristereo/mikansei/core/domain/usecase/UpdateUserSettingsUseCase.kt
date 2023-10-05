package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.ProfileSettingsField
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.model.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UpdateUserSettingsUseCase(
    private val danbooruRepository: DanbooruRepository,
    private val userRepository: UserRepository,
) {
    operator fun invoke(data: ProfileSettingsField): Flow<Result<Unit>> {
        val activeUser = userRepository.active.value

        return when {
            activeUser.id == 0 -> flow {
                val changes = activeUser.copy(
                    danbooru = activeUser.danbooru.copy(
                        safeMode = data.enableSafeMode ?: activeUser.danbooru.safeMode,
                        showDeletedPosts = data.showDeletedPosts ?: activeUser.danbooru.showDeletedPosts,
                        defaultImageSize = data.defaultImageSize ?: activeUser.danbooru.defaultImageSize,
                        blacklistedTags = data.blacklistedTags ?: activeUser.danbooru.blacklistedTags,
                    ),
                )

                userRepository.update(changes)

                emit(Result.Success(Unit))
            }

            else -> danbooruRepository.updateUserSettings(
                id = activeUser.id,
                field = data,
            )
        }
    }
}
