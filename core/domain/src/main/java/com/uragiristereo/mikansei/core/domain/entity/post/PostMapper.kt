package com.uragiristereo.mikansei.core.domain.entity.post

import com.uragiristereo.mikansei.core.danbooru.model.post.DanbooruPost
import com.uragiristereo.mikansei.core.domain.entity.UnitConverter
import com.uragiristereo.mikansei.core.model.Rating
import com.uragiristereo.mikansei.core.model.danbooru.post.Post
import com.uragiristereo.mikansei.core.model.danbooru.post.PostImage
import java.io.File
import java.util.*

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
        hasScaled = hasLarge,
        image = PostImage(
            url = fileUrl.orEmpty(),
            width = imageWidth,
            height = imageHeight,
            fileType = fileUrl?.let { File(it).extension }.orEmpty(),
        ),
        scaledImage = PostImage(
            url = largeFileUrl.orEmpty(),
            width = 850,
            height = 850 * imageWidth / imageHeight,
            fileType = largeFileUrl?.let { File(it).extension }.orEmpty(),
        ),
        previewImage = PostImage(
            url = previewFileUrl.orEmpty()
                .replace(oldValue = "preview", newValue = "720x720")
                .replace(oldValue = ".jpg", newValue = ".webp"),
            width = 720 * imageWidth / imageHeight,
            height = 720,
            fileType = "webp",
        ),
        aspectRatio = UnitConverter.coerceAspectRatio(imageWidth, imageHeight),
        tags = tagString.split(' '),
    )
}

@JvmName("DanbooruPostListToPostList")
fun List<DanbooruPost>.toPostList(): List<Post> {
    return map { item -> item.toPost() }
        .filter { item -> item.id != 0 }
}
