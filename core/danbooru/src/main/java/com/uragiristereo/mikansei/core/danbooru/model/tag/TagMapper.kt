package com.uragiristereo.mikansei.core.danbooru.model.tag

import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Tag
import com.uragiristereo.mikansei.core.model.UnitConverter

fun DanbooruTag.toTag(): Tag {
    return Tag(
        id = id,
        name = name,
        category = when (category) {
            0 -> Tag.Category.GENERAL
            1 -> Tag.Category.ARTIST
            2 -> Tag.Category.UNKNOWN
            3 -> Tag.Category.COPYRIGHT
            4 -> Tag.Category.CHARACTER
            5 -> Tag.Category.META
            else -> Tag.Category.UNKNOWN
        },
        postCount = postCount,
        postCountFormatted = UnitConverter.convertToUnit(postCount.toInt()),
    )
}

@JvmName("DanbooruTagListToTagList")
fun List<DanbooruTag>.toTagList(): List<Tag> {
    return map { it.toTag() }
}

fun DanbooruTagAutoComplete.toTag(): Tag {
    val postCount = postCount ?: 0

    return Tag(
        name = value,
        category = when (category) {
            0 -> Tag.Category.GENERAL
            1 -> Tag.Category.ARTIST
            2 -> Tag.Category.UNKNOWN
            3 -> Tag.Category.COPYRIGHT
            4 -> Tag.Category.CHARACTER
            5 -> Tag.Category.META
            else -> Tag.Category.UNKNOWN
        },
        postCount = postCount,
        postCountFormatted = UnitConverter.convertToUnit(postCount.toInt()),
        antecedent = antecedent,
    )
}

@JvmName("DanbooruTagAutoCompleteListToTagList")
fun List<DanbooruTagAutoComplete>.toTagList(): List<Tag> {
    return map { it.toTag() }
}
