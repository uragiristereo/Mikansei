package com.uragiristereo.mikansei.core.danbooru.model.post

import com.uragiristereo.mikansei.core.model.UnitConverter
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.model.danbooru.Rating

fun DanbooruPost.toPost(): Post {
    val original = mediaAsset.variants!!.first { it.type == "original" }

    return Post(
        id = id!!,
        createdAt = createdAt,
        uploaderId = uploaderId,
        source = source,
        rating = when (rating) {
            "g" -> Rating.GENERAL
            "s" -> Rating.SENSITIVE
            "q" -> Rating.QUESTIONABLE
            "e" -> Rating.EXPLICIT
            else -> Rating.EXPLICIT
        },
        status = when {
            isBanned -> Post.Status.BANNED
            isDeleted -> Post.Status.DELETED
            isPending -> Post.Status.PENDING
            else -> Post.Status.ACTIVE
        },
        score = score,
        upScore = upScore,
        downScore = downScore,
        favorites = favCount,
        type = when (original.fileExt) {
            in listOf("jpg", "jpeg", "png") -> Post.Type.IMAGE
            "gif" -> Post.Type.ANIMATED_GIF
            in listOf("mp4", "webm") -> Post.Type.VIDEO
            "zip" -> Post.Type.UGOIRA
            else -> Post.Type.IMAGE
        },
        medias = Post.Medias(
            original = original.toPostMedia(),
            scaled = mediaAsset.variants.firstOrNull { it.type == "sample" }?.toPostMedia(),
            preview = mediaAsset.variants.first { it.type == "720x720" }.toPostMedia(),
        ),
        aspectRatio = UnitConverter.coerceAspectRatio(imageWidth, imageHeight),
        tags = tagString.split(' '),
    )
}

@JvmName("DanbooruPostListToPostList")
fun List<DanbooruPost>.toPostList(): List<Post> {
    return this
        .filter { item ->
            item.id != null && item.mediaAsset.variants != null
        }.map { item ->
            item.toPost()
        }
}

fun DanbooruVariant.toPostMedia(): Post.Media {
    return Post.Media(
        url = url,
        width = width,
        height = height,
        fileType = fileExt,
    )
}
