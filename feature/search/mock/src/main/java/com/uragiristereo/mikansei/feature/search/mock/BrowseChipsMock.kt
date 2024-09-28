package com.uragiristereo.mikansei.feature.search.mock

import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Tag
import com.uragiristereo.mikansei.core.domain.module.search.BrowseChipType
import com.uragiristereo.mikansei.core.domain.module.search.TagType

val tomoChip = BrowseChipType.Single.Regular(
    tag = "ebizuka_tomo",
    category = Tag.Category.CHARACTER,
    tagType = TagType.INCLUDE,
)

val rupaChip = BrowseChipType.Single.Regular(
    tag = "rupa_(girls_band_cry)",
    category = Tag.Category.CHARACTER,
    tagType = TagType.INCLUDE,
)

val oneBoyChip = BrowseChipType.Single.Regular(
    tag = "1boy",
    category = null,
    tagType = TagType.EXCLUDE,
)

val iseriChip = BrowseChipType.Single.Regular(
    tag = "iseri",
    category = Tag.Category.ARTIST,
    tagType = TagType.INCLUDE,
)

val highresChip = BrowseChipType.Single.Regular(
    tag = "highres",
    category = Tag.Category.META,
    tagType = TagType.INCLUDE,
)

val microphoneChip = BrowseChipType.Single.Regular(
    tag = "microphone",
    category = Tag.Category.GENERAL,
    tagType = TagType.INCLUDE,
)

val ordfavChip = BrowseChipType.Single.Qualifier(
    first = "ordfav",
    second = "Mikansei_GitHub",
)

val expectedChips = listOf(
    highresChip,
    microphoneChip,
    oneBoyChip,
    BrowseChipType.Or(
        tags = listOf(
            tomoChip,
            rupaChip,
        ),
    ),
    iseriChip,
    ordfavChip,
)
