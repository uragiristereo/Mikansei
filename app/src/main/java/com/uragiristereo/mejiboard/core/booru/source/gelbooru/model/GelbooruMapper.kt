package com.uragiristereo.mejiboard.core.booru.source.gelbooru.model

import android.content.Context
import com.uragiristereo.mejiboard.core.booru.source.BooruSources
import com.uragiristereo.mejiboard.core.booru.source.gelbooru.model.post.GelbooruPostsResult
import com.uragiristereo.mejiboard.core.booru.source.gelbooru.model.search.GelbooruSearch
import com.uragiristereo.mejiboard.core.booru.source.gelbooru.model.tag.GelbooruTagsResult
import com.uragiristereo.mejiboard.core.common.data.Constants
import com.uragiristereo.mejiboard.core.common.data.util.NumberUtil
import com.uragiristereo.mejiboard.domain.entity.booru.post.Post
import com.uragiristereo.mejiboard.domain.entity.booru.post.PostImage
import com.uragiristereo.mejiboard.domain.entity.booru.post.Rating
import com.uragiristereo.mejiboard.domain.entity.booru.tag.Tag
import com.uragiristereo.mejiboard.domain.entity.booru.tag.TagType
import java.io.File

fun GelbooruPostsResult.toPostList(context: Context): List<Post> {
    val domain = context.getString(BooruSources.Gelbooru.domainResId)

    return this.post?.map {
        val fileType = File(it.image).extension

        Post(
            source = BooruSources.Gelbooru,
            id = it.id,
            scaled = it.sample == 1,
            rating = when (it.rating) {
                "general" -> Rating.GENERAL
                "sensitive" -> Rating.SENSITIVE
                "questionable" -> Rating.QUESTIONABLE
                "explicit" -> Rating.EXPLICIT
                else -> Rating.GENERAL
            },
            tags = it.tags,
            uploadedAt = it.createdAt,
            uploader = it.owner,
            imageSource = try {
                (it.source) as String
            } catch (t: Throwable) {
                ""
            },
            originalImage = PostImage(
                url = when (fileType) {
                    in Constants.SUPPORTED_TYPES_VIDEO -> "https://video-cdn3.$domain/images/${it.directory}/${it.md5}.$fileType"
                    else -> "https://img3.$domain/images/${it.directory}/${it.md5}.$fileType"
                },
                fileType = fileType,
                height = it.height,
                width = it.width,
            ),
            scaledImage = PostImage(
                url = "https://img3.$domain/samples/${it.directory}/sample_${it.md5}.jpg",
                fileType = "jpg",
                height = it.sampleHeight,
                width = it.sampleWidth,
            ),
            previewImage = PostImage(
                url = "https://img3.$domain/thumbnails/${it.directory}/thumbnail_${it.md5}.jpg",
                fileType = "jpg",
                height = it.previewHeight,
                width = it.previewWidth,
            ),
            aspectRatio = NumberUtil.coerceAspectRatio(it.width, it.height),
        )
    }
        ?: emptyList()
}

fun List<GelbooruSearch>.toTagList(): List<Tag> {
    return this.mapIndexed { index, it ->
        Tag(
            id = index,
            name = it.value,
            count = it.postCount,
            countFmt = NumberUtil.convertToUnit(it.postCount),
            type = when (it.type) {
                "tag" -> TagType.GENERAL
                "artist" -> TagType.ARTIST
                "copyright" -> TagType.COPYRIGHT
                "character" -> TagType.CHARACTER
                "metadata" -> TagType.METADATA
                else -> TagType.NONE
            },
        )
    }
}

fun GelbooruTagsResult.toTagList(): List<Tag> {
    return this.tag?.map {
        Tag(
            id = it.id,
            name = it.name,
            count = it.count,
            countFmt = NumberUtil.convertToUnit(it.count),
            type = when (it.type) {
                0 -> TagType.GENERAL
                1 -> TagType.ARTIST
                3 -> TagType.COPYRIGHT
                4 -> TagType.CHARACTER
                5 -> TagType.METADATA
                else -> TagType.NONE
            },
        )
    }
        ?: emptyList()
}
