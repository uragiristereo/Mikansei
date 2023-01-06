package com.uragiristereo.mejiboard.core.booru.source.danbooru.model

import com.uragiristereo.mejiboard.core.booru.source.danbooru.model.post.DanbooruPost
import com.uragiristereo.mejiboard.core.booru.source.danbooru.model.search.DanbooruSearch
import com.uragiristereo.mejiboard.core.booru.source.danbooru.model.tag.DanbooruTag
import com.uragiristereo.mejiboard.core.common.data.util.NumberUtil
import com.uragiristereo.mejiboard.core.model.booru.BooruSources
import com.uragiristereo.mejiboard.core.model.booru.post.Post
import com.uragiristereo.mejiboard.core.model.booru.post.PostImage
import com.uragiristereo.mejiboard.core.model.booru.post.Rating
import com.uragiristereo.mejiboard.core.model.booru.tag.Tag
import com.uragiristereo.mejiboard.core.model.booru.tag.TagType

fun List<DanbooruPost>.toPostList(): List<Post> {
    val cdnUrl = "https://cdn.donmai.us"

    return this.map {
        val directory: String
        var originalImageUrl = ""
        var scaledImageUrl = ""
        var previewImageUrl = ""

        it.md5?.let { md5 ->
            directory = "${md5.substring(0, 2)}/${md5.substring(2, 4)}"
            originalImageUrl = "$cdnUrl/original/$directory/$md5.${it.fileExt}"
            scaledImageUrl = "$cdnUrl/sample/$directory/sample-$md5.jpg"
            previewImageUrl = "$cdnUrl/preview/$directory/$md5.jpg"
        }

        Post(
            source = BooruSources.Danbooru,
            id = it.id ?: 0,
            scaled = it.hasLarge ?: false,
            rating = when (it.rating) {
                "g" -> Rating.GENERAL
                "s" -> Rating.SENSITIVE
                "q" -> Rating.QUESTIONABLE
                "e" -> Rating.EXPLICIT
                else -> Rating.GENERAL
            },
            tags = it.tagString,
            uploadedAt = it.createdAt,
            uploader = it.uploaderId.toString(),
            imageSource = it.source,
            originalImage = PostImage(
                url = originalImageUrl,
                fileType = it.fileExt,
                height = it.imageHeight,
                width = it.imageWidth,
            ),
            scaledImage = PostImage(
                url = scaledImageUrl,
                fileType = "jpg",
                height = it.imageHeight,
                width = it.imageWidth,
            ),
            previewImage = PostImage(
                url = previewImageUrl,
                fileType = "jpg",
                height = 180,
                width = ((180f / it.imageHeight) * it.imageWidth).toInt(),
            ),
            aspectRatio = NumberUtil.coerceAspectRatio(it.imageWidth, it.imageHeight),
        )
    }
}

fun List<DanbooruSearch>.toTagList(): List<Tag> {
    return this.mapIndexed { index, it ->
        Tag(
            id = index,
            name = it.antecedent ?: it.value,
            count = it.postCount,
            countFmt = NumberUtil.convertToUnit(it.postCount),
            type = when (it.category) {
                0 -> TagType.GENERAL
                1 -> TagType.ARTIST
                3 -> TagType.COPYRIGHT
                4 -> TagType.CHARACTER
                5 -> TagType.METADATA
                else -> TagType.NONE
            },
        )
    }
}

@JvmName("toTagListDanbooruTag")
fun List<DanbooruTag>.toTagList(): List<Tag> {
    return this.mapIndexed { index, it ->
        Tag(
            id = index,
            name = it.name,
            count = it.postCount,
            countFmt = NumberUtil.convertToUnit(it.postCount),
            type = when (it.category) {
                0 -> TagType.GENERAL
                1 -> TagType.ARTIST
                3 -> TagType.COPYRIGHT
                4 -> TagType.CHARACTER
                5 -> TagType.METADATA
                else -> TagType.NONE
            },
        )
    }
}
