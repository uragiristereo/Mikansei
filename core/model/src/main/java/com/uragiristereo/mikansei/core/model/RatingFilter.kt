package com.uragiristereo.mikansei.core.model

import com.uragiristereo.mikansei.core.model.booru.post.Rating

object RatingFilter {
    // this lists ratings that to be FILTERED
    val GENERAL_ONLY = listOf(Rating.SENSITIVE, Rating.QUESTIONABLE, Rating.EXPLICIT)
    val SAFE = listOf(Rating.QUESTIONABLE, Rating.EXPLICIT)
    val NO_EXPLICIT = listOf(Rating.EXPLICIT)
    val UNFILTERED = listOf<Rating>()

    val map = mapOf(
        "general_only" to GENERAL_ONLY,
        "safe" to SAFE,
        "no_explicit" to NO_EXPLICIT,
        "unfiltered" to UNFILTERED,
    )

    val list = map.map { it.value }

    fun getItemsByKey(key: String): List<Rating> {
        return map[key] ?: SAFE
    }

    fun itemsToKey(filter: List<Rating>): String {
        return when (filter) {
            GENERAL_ONLY -> "general_only"
            SAFE -> "safe"
            NO_EXPLICIT -> "no_explicit"
            UNFILTERED -> "unfiltered"
            else -> "safe"
        }
    }
}
