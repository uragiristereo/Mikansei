package com.uragiristereo.mejiboard.core.database.dao.session

import com.uragiristereo.mejiboard.core.model.booru.BooruSource
import com.uragiristereo.mejiboard.core.model.booru.getBooruByKey
import com.uragiristereo.mejiboard.core.model.booru.post.Post
import java.util.UUID

fun List<PostSession>.toPostList(): List<Post> {
    return map {
        Post(
            source = BooruSource.values().getBooruByKey(key = it.source) ?: BooruSource.Gelbooru,
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
