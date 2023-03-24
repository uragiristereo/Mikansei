package com.uragiristereo.mikansei.core.database.dao.session

import com.uragiristereo.mikansei.core.model.danbooru.post.Post
import java.util.*

fun SessionRow.toPost(): Post {
    return Post(
        id = post.id,
        createdAt = post.createdAt,
        uploaderId = post.uploaderId,
        source = post.source,
        pixivId = post.pixivId,
        rating = post.rating,
        score = post.score,
        upScore = post.upScore,
        downScore = post.downScore,
        favoriteCount = post.favoriteCount,
        isPending = post.isPending,
        isDeleted = post.isDeleted,
        hasScaled = post.hasScaled,
        image = post.image,
        scaledImage = post.scaledImage,
        previewImage = post.previewImage,
        aspectRatio = post.aspectRatio,
        tags = post.tags,
    )
}

fun List<SessionRow>.toPostList(): List<Post> {
    return map { it.toPost() }
}

fun Post.toSession(
    sessionUuid: String,
    sequence: Int,
): SessionRow {
    return SessionRow(
        uuid = UUID.randomUUID().toString(),
        sessionUuid = sessionUuid,
        sequence = sequence,
        post = this,
    )
}

fun List<Post>.toSessionList(sessionUuid: String): List<SessionRow> {
    return mapIndexed { index, post ->
        post.toSession(
            sessionUuid = sessionUuid,
            sequence = index,
        )
    }
}
