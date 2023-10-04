package com.uragiristereo.mikansei.core.danbooru.model.favorite

import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Favorite

fun DanbooruFavoriteGroup.toFavorite(): Favorite {
    return Favorite(
        id = id,
        name = name.replace(oldChar = '_', newChar = ' '),
        thumbnailUrl = null,
        postIds = postIds,
    )
}

fun List<DanbooruFavoriteGroup>.toFavoriteList(): List<Favorite> {
    return map { it.toFavorite() }
}
