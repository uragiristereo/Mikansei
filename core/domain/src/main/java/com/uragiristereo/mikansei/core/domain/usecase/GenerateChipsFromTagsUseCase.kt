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

        while (true) {
            val orStartIndex = when {
                tagsResult.startsWith('(') -> 0
                tagsResult.indexOf(" (") == -1 -> -1
                else -> tagsResult.indexOf(" (") + 1
            }

            val orEndIndex = tagsResult.indexOf(") ").let {
                if (it == -1 && tagsResult.endsWith(')')) {
                    tagsResult.lastIndex
                } else {
                    it
                }
            }

            if (orStartIndex >= 0 && orEndIndex >= 0) {
                var orTags = tagsResult.substring(orStartIndex, orEndIndex + 1)
                val orSplit = orTags.split("__or__")
                val orCount = orSplit.size - 1

                if (orCount > 0) {
                    orTags = orTags.removeSurrounding(prefix = "(", suffix = ")")
                    tagsResult = tagsResult.replaceRange(
                        startIndex = orStartIndex,
                        endIndex = orEndIndex + 1,
                        replacement = orTags,
                    )
                } else {
                    break
                }
            } else {
                break
            }
        }

        val result = tagsResult
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
