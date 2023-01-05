package com.uragiristereo.mejiboard.core.booru.source.safebooruorg.model

import android.content.Context
import com.uragiristereo.mejiboard.core.booru.source.BooruSources
import com.uragiristereo.mejiboard.core.booru.source.safebooruorg.model.posts.SafebooruOrgPost
import com.uragiristereo.mejiboard.core.booru.source.safebooruorg.model.search.SafebooruOrgSearch
import com.uragiristereo.mejiboard.core.common.data.util.NumberUtil
import com.uragiristereo.mejiboard.domain.entity.booru.post.Post
import com.uragiristereo.mejiboard.domain.entity.booru.post.PostImage
import com.uragiristereo.mejiboard.domain.entity.booru.post.Rating
import com.uragiristereo.mejiboard.domain.entity.booru.tag.Tag
import com.uragiristereo.mejiboard.domain.entity.booru.tag.TagType
import java.io.File
import java.util.Date

fun List<SafebooruOrgPost>.toPostList(context: Context): List<Post> {
    val baseUrl = BooruSources.SafebooruOrg.baseUrl(context)

    return this.map {
        val fileNameWithoutExtension = it.image
            .substring(
                startIndex = 0,
                endIndex = it.image.lastIndexOf(char = '.'),
            )

        Post(
            source = BooruSources.SafebooruOrg,
            id = it.id,
            scaled = it.sample,
            rating = Rating.GENERAL,
            tags = it.tags,
            uploadedAt = Date(it.change * 1000L),
            uploader = it.owner,
            imageSource = "",
            originalImage = PostImage(
                url = "$baseUrl/images/${it.directory}/${it.image}",
                fileType = File(it.image).extension,
                height = it.height,
                width = it.width,
            ),
            scaledImage = PostImage(
                url = "$baseUrl/samples/${it.directory}/sample_$fileNameWithoutExtension.jpg",
                fileType = "jpg",
                height = it.sampleHeight,
                width = it.sampleWidth,
            ),
            previewImage = PostImage(
                url = "$baseUrl/thumbnails/${it.directory}/thumbnail_$fileNameWithoutExtension.jpg",
                fileType = "jpg",
                height = 150,
                width = ((150f / it.height) * it.width).toInt(),
            ),
            aspectRatio = NumberUtil.coerceAspectRatio(it.width, it.height),
        )
    }
}

fun List<SafebooruOrgSearch>.toTagList(): List<Tag> {
    return this.mapIndexed { index, it ->
        val count = try {
            it.label
                .replace(oldValue = "${it.value} ", newValue = "")
                .replace(oldValue = "(", newValue = "")
                .replace(oldValue = ")", newValue = "")
                .toInt()
        } catch (_: Throwable) {
            0
        }

        Tag(
            id = index,
            name = it.value,
            count = count,
            countFmt = NumberUtil.convertToUnit(count),
            type = TagType.NONE,
        )
    }
}
