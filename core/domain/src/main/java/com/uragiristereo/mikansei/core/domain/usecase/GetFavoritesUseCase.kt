package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.danbooru.repository.DanbooruRepository
import com.uragiristereo.mikansei.core.database.dao.user.UserDao
import com.uragiristereo.mikansei.core.database.dao.user.toUser
import com.uragiristereo.mikansei.core.domain.entity.favorite.Favorite
import com.uragiristereo.mikansei.core.domain.entity.post.toPost
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.model.result.mapSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class GetFavoritesUseCase(
    private val danbooruRepository: DanbooruRepository,
    private val userDao: UserDao,
) {
    suspend operator fun invoke(): Flow<Result<Favorite>> {
        val activeUser = userDao.getActive().first().toUser()

        return danbooruRepository.getPosts(tags = "ordfav:${activeUser.name}", page = 1)
            .mapSuccess { danbooruPosts ->
                val thumbnailUrl = when {
                    danbooruPosts.isNotEmpty() -> {
                        val firstPost = danbooruPosts.first().toPost()

                        firstPost.previewImage.url
                    }

                    else -> null
                }

                Favorite(
                    id = 0,
                    name = "My favorites",
                    thumbnailUrl = thumbnailUrl,
                )
            }
    }
}
