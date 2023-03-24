package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.danbooru.repository.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.entity.post.toPost
import com.uragiristereo.mikansei.core.model.danbooru.post.Post
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.model.result.mapSuccess
import kotlinx.coroutines.flow.Flow

class GetPostUseCase(
    private val danbooruRepository: DanbooruRepository,
) {
    suspend operator fun invoke(id: Int): Flow<Result<Post>> {
        return danbooruRepository.getPost(id)
            .mapSuccess { it.toPost() }
    }
}
