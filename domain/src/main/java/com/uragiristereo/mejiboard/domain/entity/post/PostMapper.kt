package com.uragiristereo.mejiboard.domain.entity.post

import com.uragiristereo.mejiboard.domain.entity.UnitConverter
import com.uragiristereo.mikansei.core.danbooru.model.post.DanbooruPost

fun DanbooruPost.toPost(): Post {
    return Post(
        id = id ?: 0,
        createdAt = createdAt,
        uploaderId = uploaderId,
        source = source,
        score = score,
        upScore = upScore,
        downScore = downScore,
        favoriteCount = favCount,
        hasScaled = hasLarge,
        imageWidth = imageWidth,
        imageHeight = imageHeight,
        aspectRatio = UnitConverter.coerceAspectRatio(imageWidth, imageHeight),
        fileType = fileExt,
        originalFileUrl = fileUrl ?: "",
        scaledFileUrl = largeFileUrl ?: "",
        previewFileUrl = previewFileUrl ?: "",
        tags = tagString.split(' '),
    )
}

fun List<DanbooruPost>.toPostList(): List<Post> {
    return map { item -> item.toPost() }
        .filter { item -> item.id != 0 }
}
