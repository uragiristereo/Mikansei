package com.uragiristereo.mikansei.core.danbooru.model.favorite

import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Favorite

fun DanbooruFavoriteGroup.toFavoriteGroup(): Favorite.Group {
    return Favorite.Group(
        id = id,
        name = name.replace(oldChar = '_', newChar = ' '),
        postIds = postIds,
    )
}

fun List<DanbooruFavoriteGroup>.toFavoriteGroupList(): List<Favorite.Group> {
    return map { it.toFavoriteGroup() }
}
