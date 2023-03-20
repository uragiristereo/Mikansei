package com.uragiristereo.mejiboard.domain.entity.post

import com.uragiristereo.mejiboard.core.database.dao.session.SessionRow
import com.uragiristereo.mejiboard.core.model.Rating
import com.uragiristereo.mejiboard.domain.entity.UnitConverter
import com.uragiristereo.mikansei.core.danbooru.model.post.DanbooruPost
import java.io.File
import java.util.UUID

fun DanbooruPost.toPost(): Post {
    return Post(
        id = id ?: 0,
        createdAt = createdAt,
        uploaderId = uploaderId,
        source = source,
        pixivId = pixivId,
        rating = when (rating) {
            "g" -> Rating.GENERAL
            "s" -> Rating.SENSITIVE
            "q" -> Rating.QUESTIONABLE
            "e" -> Rating.EXPLICIT
            else -> Rating.EXPLICIT
        },
        score = score,
        upScore = upScore,
        downScore = downScore,
        favoriteCount = favCount,
        isPending = isPending,
        isDeleted = isDeleted,
        imageUrl = fileUrl,
        imageWidth = imageWidth,
        imageHeight = imageHeight,
        imageFileType = fileUrl?.let { File(it).extension },
        scaledImageUrl = largeFileUrl,
        scaledImageWidth = 850,
        scaledImageHeight = 850 * imageWidth / imageHeight,
        scaledImageFileType = largeFileUrl?.let { File(it).extension },
        previewImageUrl = previewFileUrl,
        previewImageWidth = 180 * imageWidth / imageHeight,
        previewImageHeight = 180,
        previewImageFileType = previewFileUrl?.let { File(it).extension },
        aspectRatio = UnitConverter.coerceAspectRatio(imageWidth, imageHeight),
        tags = tagString.split(' '),
    )
}

fun List<DanbooruPost>.toPostList(): List<Post> {
    return map { item -> item.toPost() }
        .filter { item -> item.id != 0 }
}

fun SessionRow.toPost(): Post {
    return Post(
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
        imageUrl = imageUrl,
        imageWidth = imageWidth,
        imageHeight = imageHeight,
        imageFileType = imageFileType,
        scaledImageUrl = scaledImageUrl,
        scaledImageWidth = scaledImageWidth,
        scaledImageHeight = scaledImageHeight,
        scaledImageFileType = scaledImageFileType,
        previewImageUrl = previewImageUrl,
        previewImageWidth = previewImageWidth,
        previewImageHeight = previewImageHeight,
        previewImageFileType = previewImageFileType,
        aspectRatio = aspectRatio,
        tags = tags.split(' '),
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
        imageUrl = imageUrl,
        imageWidth = imageWidth,
        imageHeight = imageHeight,
        imageFileType = imageFileType,
        scaledImageUrl = scaledImageUrl,
        scaledImageWidth = scaledImageWidth,
        scaledImageHeight = scaledImageHeight,
        scaledImageFileType = scaledImageFileType,
        previewImageUrl = previewImageUrl,
        previewImageWidth = previewImageWidth,
        previewImageHeight = previewImageHeight,
        previewImageFileType = previewImageFileType,
        aspectRatio = aspectRatio,
        tags = tags.joinToString(separator = " "),
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
