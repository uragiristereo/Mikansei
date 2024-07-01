package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Favorite
import com.uragiristereo.mikansei.core.domain.module.database.SessionRepository
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.model.result.mapSuccess
import java.util.UUID

class GetFavoritesUseCase(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository,
    private val getPostsUseCase: GetPostsUseCase,
) {
    suspend operator fun invoke(): Result<Favorite> {
        val activeUser = userRepository.active.value
        val sessionId = UUID.randomUUID().toString()

        return getPostsUseCase(
            tags = "ordfav:${activeUser.name}",
            page = 1,
            sessionId = sessionId,
        ).mapSuccess { postsResult ->
            val posts = postsResult.posts

            val thumbnailUrl = when {
                posts.isNotEmpty() -> {
                    val firstPost = posts.first()

                    firstPost.medias.preview.url
                }

                else -> null
            }

            sessionRepository.delete(sessionId)

            Favorite(
                id = 0,
                name = "My favorites",
                thumbnailUrl = thumbnailUrl,
                postIds = posts.map { it.id },
            )
        }
    }
}
