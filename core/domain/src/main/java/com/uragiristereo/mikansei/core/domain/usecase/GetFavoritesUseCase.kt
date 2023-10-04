package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.database.dao.user.UserDao
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Favorite
import com.uragiristereo.mikansei.core.domain.module.database.model.toProfile
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.model.result.mapSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class GetFavoritesUseCase(
    private val userDao: UserDao,
    private val getPostsUseCase: GetPostsUseCase,
) {
    suspend operator fun invoke(): Flow<Result<Favorite>> {
        val activeUser = userDao.getActive().first().toProfile()

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
