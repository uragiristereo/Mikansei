package com.uragiristereo.mejiboard.domain.entity.tag

import com.uragiristereo.mejiboard.domain.entity.UnitConverter
import com.uragiristereo.mikansei.core.danbooru.model.tag.DanbooruTag
import com.uragiristereo.mikansei.core.danbooru.model.tag.DanbooruTagAutoComplete

fun DanbooruTag.toTag(): Tag {
    return Tag(
        id = id,
        name = name,
        category = when (category) {
            0 -> TagCategory.GENERAL
            1 -> TagCategory.ARTIST
            2 -> TagCategory.UNKNOWN
            3 -> TagCategory.COPYRIGHT
            4 -> TagCategory.CHARACTER
            5 -> TagCategory.META
            else -> TagCategory.UNKNOWN
        },
        postCount = postCount,
        postCountFormatted = UnitConverter.convertToUnit(postCount.toInt()),
    )
}

fun List<DanbooruTag>.toTagList(): List<Tag> {
    return map { item -> item.toTag() }
}

fun DanbooruTagAutoComplete.toTagAutoComplete(): TagAutoComplete {
    return TagAutoComplete(
        value = value,
        category = when (category) {
            0 -> TagCategory.GENERAL
            1 -> TagCategory.ARTIST
            2 -> TagCategory.UNKNOWN
            3 -> TagCategory.COPYRIGHT
            4 -> TagCategory.CHARACTER
            5 -> TagCategory.META
            else -> TagCategory.UNKNOWN
        },
        postCount = postCount,
        postCountFormatted = UnitConverter.convertToUnit(postCount.toInt()),
        antecedent = antecedent,
    )
}

fun List<DanbooruTagAutoComplete>.toTagAutoCompleteList(): List<TagAutoComplete> {
    return map { item -> item.toTagAutoComplete() }
}