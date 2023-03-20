package com.uragiristereo.mejiboard.domain.entity.post

import com.uragiristereo.mejiboard.core.model.Rating
import com.uragiristereo.mejiboard.domain.entity.UnitConverter
import com.uragiristereo.mikansei.core.danbooru.model.post.DanbooruPost
import java.io.File

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
