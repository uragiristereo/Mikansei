package com.uragiristereo.mikansei.core.domain.module.search

import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Tag

sealed interface BrowseChipType {
    sealed interface Single : BrowseChipType {
        data class Regular(
            val tag: String,
            val category: Tag.Category?,
            val tagType: TagType,
        ) : Single

        data class Qualifier(val first: String, val second: String) : Single
    }

    data class Or(val tags: List<Single>) : BrowseChipType
}
