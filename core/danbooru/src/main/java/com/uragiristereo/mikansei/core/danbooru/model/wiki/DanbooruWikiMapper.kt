package com.uragiristereo.mikansei.core.danbooru.model.wiki

import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Wiki

fun DanbooruWikiResponse.toWiki(): Wiki {
    return Wiki(
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt,
        title = title,
        body = body,
        isLocked = isLocked,
        isDeleted = isDeleted,
        otherNames = otherNames,
    )
}
