package com.uragiristereo.mikansei.core.domain.entity.post

import com.uragiristereo.mikansei.core.danbooru.model.post.DanbooruPost
import com.uragiristereo.mikansei.core.danbooru.model.post.DanbooruVariant
import com.uragiristereo.mikansei.core.domain.entity.UnitConverter
import com.uragiristereo.mikansei.core.model.danbooru.Rating
import com.uragiristereo.mikansei.core.model.danbooru.post.Post
import com.uragiristereo.mikansei.core.model.danbooru.post.PostImage

fun DanbooruPost.toPost(): Post {
    val image = mediaAsset.variants!!.first { it.type == "original" }.toPostImage()

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
        isBanned = isBanned,
        hasScaled = hasLarge,
        image = image,
        scaledImage = when {
            hasLarge -> mediaAsset.variants!!.first { it.type == "sample" }.toPostImage()
            else -> image
        },
        previewImage = mediaAsset.variants!!.first { it.type == "720x720" }.toPostImage(),
        aspectRatio = UnitConverter.coerceAspectRatio(imageWidth, imageHeight),
        tags = tagString.split(' '),
    )
}

@JvmName("DanbooruPostListToPostList")
fun List<DanbooruPost>.toPostList(): List<Post> {
    return filter { item ->
        item.id != null && item.mediaAsset.variants != null
    }.map { item -> item.toPost() }
}

private fun DanbooruVariant.toPostImage(): PostImage {
    return PostImage(
        url = url,
        width = width,
        height = height,
        fileType = fileExt,
    )
}
