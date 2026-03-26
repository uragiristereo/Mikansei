package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Favorite
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.model.result.mapSuccess

class GetFavoritesUseCase(
    private val userRepository: UserRepository,
    private val danbooruRepository: DanbooruRepository,
) {
    suspend operator fun invoke(): Result<Favorite.Regular> {
        val activeUser = userRepository.active.value

        return danbooruRepository.getPosts(
            tags = "ordfav:${activeUser.name}",
            page = 1,
        ).mapSuccess { postsResult ->
            val posts = postsResult.posts

            Favorite.Regular(
                id = 0,
                name = "My favorites",
                posts = posts,
                preferredThumbnailPostId = posts.firstOrNull()?.id,
            )
        }
    }
}
