package com.uragiristereo.mejiboard.core.database.dao.session

import com.uragiristereo.mejiboard.core.booru.source.BooruSources
import com.uragiristereo.mejiboard.domain.entity.booru.post.Post
import java.util.UUID

fun List<PostSession>.toPostList(): List<Post> {
    return map {
        Post(
            source = BooruSources.getBooruByKey(key = it.source) ?: BooruSources.Gelbooru,
            id = it.id,
            scaled = it.scaled,
            rating = it.rating,
            tags = it.tags,
            uploadedAt = it.uploadedAt,
            uploader = it.uploader,
            imageSource = it.imageSource,
            originalImage = it.originalImage,
            scaledImage = it.scaledImage,
            previewImage = it.previewImage,
            aspectRatio = it.aspectRatio,
        )
    }
}

fun List<Post>.toPostSessionList(
    sessionId: String,
): List<PostSession> {
    return mapIndexed { index, it ->
        PostSession(
            uuid = UUID.randomUUID().toString(),
            sessionId = sessionId,
            sequence = index,
            source = it.source.key,
            id = it.id,
            scaled = it.scaled,
            rating = it.rating,
            tags = it.tags,
            uploadedAt = it.uploadedAt,
            uploader = it.uploader,
            imageSource = it.imageSource,
            originalImage = it.originalImage,
            scaledImage = it.scaledImage,
            previewImage = it.previewImage,
            aspectRatio = it.aspectRatio,
        )
    }
}
