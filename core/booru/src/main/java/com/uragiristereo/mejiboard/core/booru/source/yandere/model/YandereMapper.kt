package com.uragiristereo.mejiboard.core.booru.source.yandere.model

import com.uragiristereo.mejiboard.core.booru.source.BooruSourceConverters
import com.uragiristereo.mejiboard.core.booru.source.yandere.model.post.YanderePost
import com.uragiristereo.mejiboard.core.booru.source.yandere.model.search.YandereSearch
import com.uragiristereo.mejiboard.core.booru.source.yandere.model.tag.YandereTags
import com.uragiristereo.mejiboard.core.model.booru.BooruSources
import com.uragiristereo.mejiboard.core.model.booru.post.Post
import com.uragiristereo.mejiboard.core.model.booru.post.PostImage
import com.uragiristereo.mejiboard.core.model.booru.post.Rating
import com.uragiristereo.mejiboard.core.model.booru.tag.Tag
import com.uragiristereo.mejiboard.core.model.booru.tag.TagType
import java.io.File
import java.util.Date

fun List<YanderePost>.toPostList(): List<Post> {
    return map {
        Post(
            source = BooruSources.Yandere,
            id = it.id,
            scaled = true,
            rating = when (it.rating) {
                "s" -> Rating.GENERAL
                "q" -> Rating.QUESTIONABLE
                "e" -> Rating.EXPLICIT
                else -> Rating.EXPLICIT
            },
            tags = it.tags,
            uploadedAt = Date(it.createdAt * 1000L),
            uploader = it.author,
            imageSource = it.source,
            originalImage = PostImage(
                url = it.fileUrl,
                fileType = File(it.fileUrl).extension,
                height = it.height,
                width = it.width,
            ),
            scaledImage = PostImage(
                url = it.sampleUrl,
                fileType = File(it.sampleUrl).extension,
                height = it.sampleHeight,
                width = it.sampleWidth,
            ),
            previewImage = PostImage(
                url = it.previewUrl,
                fileType = File(it.previewUrl).extension,
                height = it.previewHeight,
                width = it.previewWidth,
            ),
            aspectRatio = BooruSourceConverters.coerceAspectRatio(
                width = it.width,
                height = it.height,
            ),
        )
    }
}

fun List<YandereSearch>.toTagList(): List<Tag> {
    return map {
        Tag(
            id = it.id,
            name = it.name,
            count = it.count,
            countFmt = BooruSourceConverters.convertToUnit(it.count),
            type = when (it.type) {
                else -> TagType.NONE
            }
        )
    }
}

fun YandereTags.toTagList(): List<Tag> {
    return keys.mapIndexed { index, tag ->
        val alternatives = this[tag]!!
        val count = (alternatives[0][1]).toInt()

        Tag(
            id = index,
            name = tag,
            count = count,
            countFmt = BooruSourceConverters.convertToUnit(count),
            type = TagType.GENERAL,
        )
    }
}
