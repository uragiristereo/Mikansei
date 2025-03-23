package com.uragiristereo.mikansei.core.danbooru.model.post

import com.uragiristereo.mikansei.core.model.UnitConverter
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.model.danbooru.Rating

fun DanbooruPost.toPost(): Post {
    val original = mediaAsset.variants!!.first { it.type == "original" }
    val preview = mediaAsset.variants.firstOrNull { it.type == "720x720" }?.toPostMedia()

    return Post(
        id = id!!,
        createdAt = createdAt,
        uploaderId = uploaderId,
        source = when {
            pixivId != null -> "https://pixiv.net/artworks/$pixivId"
            else -> source
        },
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
        relationshipType = when {
            hasChildren && parentId != null -> Post.RelationshipType.PARENT_CHILD
            hasChildren -> Post.RelationshipType.PARENT
            parentId != null -> Post.RelationshipType.CHILD
            else -> Post.RelationshipType.NONE
        },
        score = score,
        upScore = upScore,
        downScore = downScore,
        favorites = favCount,
        type = when (original.fileExt) {
            in listOf("jpg", "jpeg", "png", "avif", "webp") -> Post.Type.IMAGE
            "gif" -> Post.Type.ANIMATED_GIF
            in listOf("mp4", "webm") -> Post.Type.VIDEO
            "zip" -> Post.Type.UGOIRA
            "swf" -> Post.Type.FLASH
            else -> Post.Type.UNSUPPORTED
        },
        medias = Post.Medias(
            original = original.toPostMedia(),
            scaled = mediaAsset.variants.firstOrNull { it.type == "sample" }?.toPostMedia(),
            preview = when {
                preview != null -> preview
                original.fileExt == "swf" -> flashPreviewPostMedia
                else -> throw UnsupportedOperationException("Unsupported post type, got: ${original.fileExt}")
            },
        ),
        aspectRatio = when {
            original.fileExt == "swf" -> 528f / 720f
            else -> UnitConverter.coerceAspectRatio(imageWidth, imageHeight)
        },
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

private val flashPreviewPostMedia = Post.Media(
    url = "https://danbooru.donmai.us/images/flash-preview.png",
    width = 528,
    height = 720,
    fileType = "swf",
)
