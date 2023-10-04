package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.database.dao.user.UserDao
import com.uragiristereo.mikansei.core.database.dao.user.UserRow
import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.model.preferences.user.RatingPreference
import com.uragiristereo.mikansei.core.model.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PerformLoginUseCase(
    private val danbooruRepository: DanbooruRepository,
    private val userDao: UserDao,
) {
    suspend operator fun invoke(
        name: String,
        apiKey: String,
    ): Flow<Result<Boolean>> {
        return danbooruRepository.login(name, apiKey)
            .map { result ->
                when (result) {
                    is Result.Success -> {
                        val profile = result.data
                        val userExists = userDao.isUserExists(profile.id)

                        when {
                            profile.name != name -> Result.Failed(message = "Invalid name/API key.")
                            userExists -> Result.Failed("User is already logged in.")
                            else -> {
                                profile.apply {
                                    userDao.add(
                                        UserRow(
                                            id = id,
                                            name = name,
                                            apiKey = apiKey,
                                            level = level.id,
                                            safeMode = danbooru.safeMode,
                                            showDeletedPosts = danbooru.showDeletedPosts,
                                            defaultImageSize = danbooru.defaultImageSize.getEnumForDanbooru(),
                                            blacklistedTags = danbooru.blacklistedTags.joinToString("\n"),
                                            postsRatingFilter = RatingPreference.GENERAL_ONLY,
                                        )
                                    )

                                    userDao.switchActiveUser(id)
                                }

                                Result.Success(true)
                            }
                        }
                    }

                    is Result.Failed -> Result.Failed(result.message)
                    is Result.Error -> Result.Error(result.t)
                }
            }
    }
}
