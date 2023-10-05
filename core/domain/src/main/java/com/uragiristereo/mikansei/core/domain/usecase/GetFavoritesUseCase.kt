package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Favorite
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.model.result.mapSuccess
import kotlinx.coroutines.flow.Flow

class GetFavoritesUseCase(
    private val userRepository: UserRepository,
    private val getPostsUseCase: GetPostsUseCase,
) {
    operator fun invoke(): Flow<Result<Favorite>> {
        val activeUser = userRepository.active.value

        return getPostsUseCase(
            tags = "ordfav:${activeUser.name}",
            page = 1,
            currentPosts = listOf(),
        ).mapSuccess { postsResult ->
            val posts = postsResult.posts

            val thumbnailUrl = when {
                posts.isNotEmpty() -> {
                    val firstPost = posts.first()

                    firstPost.medias.preview.url
                }

                else -> null
            }

            Favorite(
                id = 0,
                name = "My favorites",
                thumbnailUrl = thumbnailUrl,
                postIds = posts.map { it.id },
            )
        }
    }
}
