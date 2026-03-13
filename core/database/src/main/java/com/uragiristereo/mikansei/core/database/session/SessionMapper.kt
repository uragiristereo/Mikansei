package com.uragiristereo.mikansei.core.database.session

import com.uragiristereo.mikansei.core.domain.module.database.entity.Session

fun SessionRow.toSession(): Session {
    return Session(
        id = id,
        tags = tags,
        scrollIndex = scrollIndex,
        scrollOffset = scrollOffset,
        page = page,
        canLoadMore = canLoadMore,
    )
}

fun Session.toSessionRow(): SessionRow {
    return SessionRow(
        id = id,
        tags = tags,
        scrollIndex = scrollIndex,
        scrollOffset = scrollOffset,
        page = page,
        canLoadMore = canLoadMore,
    )
}
