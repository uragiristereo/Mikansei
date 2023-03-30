package com.uragiristereo.mikansei.core.database.dao.session

import com.uragiristereo.mikansei.core.model.danbooru.post.Post
import java.util.*

fun SessionRow.toPost(): Post {
    return post.apply {
        Post(
            id = id,
            createdAt = createdAt,
            uploaderId = uploaderId,
            source = source,
            pixivId = pixivId,
            rating = rating,
            score = score,
            upScore = upScore,
            downScore = downScore,
            favoriteCount = favoriteCount,
            isPending = isPending,
            isDeleted = isDeleted,
            isBanned = isBanned,
            hasScaled = hasScaled,
            image = image,
            scaledImage = scaledImage,
            previewImage = previewImage,
            aspectRatio = aspectRatio,
            tags = tags,
        )
    }
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
