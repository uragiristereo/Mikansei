package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Tag
import com.uragiristereo.mikansei.core.domain.module.search.BrowseChipType
import com.uragiristereo.mikansei.core.domain.module.search.TagType

class GenerateChipsFromTagsUseCase {
    operator fun invoke(
        tags: String,
        cachedTags: Map<String, Tag.Category>,
    ): List<BrowseChipType> {
        var tagsResult = tags
            .trim()
            .replace(regex = "\\s+".toRegex(), replacement = " ")
            .replace(" or ", "__or__")

        if (tagsResult.isEmpty()) {
            return emptyList()
        }
        if (tagsResult.startsWith("(")) {
            tagsResult = " $tagsResult"
        }
        if (tagsResult.endsWith(")")) {
            tagsResult = "$tagsResult "
        }

        var startFrom = 0
        val orTags = mutableListOf<String>()

        while (true) {
            val startIndex = tagsResult.substring(startFrom).indexOf(" (")
            if (startIndex == -1) {
                break
            }
            val finalStartIndex = startIndex + startFrom + 1

            val endIndex = tagsResult.substring(finalStartIndex).indexOf(") ")
            if (endIndex == -1) {
                break
            }
            val finalEndIndex = endIndex + finalStartIndex + 1

            val tag = tagsResult.substring(finalStartIndex, finalEndIndex)
            startFrom = finalEndIndex
            orTags.add(tag)
        }
        var tags2 = tagsResult

        orTags.forEach {
            val withoutBraces = it.removeSurrounding("(", ")")
            tags2 = tags2.replaceFirst(it, withoutBraces)
        }

        val result = tags2
            .trim()
            .split(" ")
            .map {
                it.split("__or__").map { singleTag ->
                    var tag = singleTag
                    val isQualifier = qualifiers.any { qualifier ->
                        tag.startsWith(qualifier)
                    }

                    if (isQualifier) {
                        val first = tag.substring(startIndex = 0, endIndex = tag.indexOf(':'))
                        val second = tag.substring(startIndex = tag.indexOf(':') + 1)

                        BrowseChipType.Single.Qualifier(first, second)
                    } else {
                        val isExclude = tag.firstOrNull() == '-'
                        if (isExclude) {
                            tag = tag.substring(startIndex = 1)
                        }

                        BrowseChipType.Single.Regular(
                            tag = tag,
                            category = cachedTags[tag],
                            tagType = when {
                                isExclude -> TagType.EXCLUDE
                                else -> TagType.INCLUDE
                            },
                        )
                    }
                }
            }
            .map { browseChipType ->
                if (browseChipType.size == 1) {
                    browseChipType.first()
                } else {
                    BrowseChipType.Or(browseChipType)
                }
            }

        return result
    }

    private companion object {
        val qualifiers = listOf(
            "search:",
            "ordfav:",
        )
    }
}
