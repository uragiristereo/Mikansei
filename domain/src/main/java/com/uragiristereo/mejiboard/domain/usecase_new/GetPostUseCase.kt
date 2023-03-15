package com.uragiristereo.mejiboard.domain.usecase_new

import com.uragiristereo.mejiboard.domain.entity.post.Post
import com.uragiristereo.mejiboard.domain.entity.post.toPost
import com.uragiristereo.mikansei.core.danbooru.repository.DanbooruRepository
import com.uragiristereo.mikansei.core.danbooru.result.Result
import com.uragiristereo.mikansei.core.danbooru.result.mapSuccess
import kotlinx.coroutines.flow.Flow

class GetPostUseCase(
    private val danbooruRepository: DanbooruRepository,
) {
    suspend operator fun invoke(id: Int): Flow<Result<Post>> {
        return danbooruRepository.getPost(id)
            .mapSuccess { it.toPost() }
    }
}
