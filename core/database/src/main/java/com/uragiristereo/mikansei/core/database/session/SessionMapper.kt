package com.uragiristereo.mikansei.core.database.session

import com.uragiristereo.mikansei.core.model.danbooru.Post
import java.util.UUID

fun Post.toSession(
    sessionUuid: String,
    sequence: Int,
): SessionRow {
    return SessionRow(
        uuid = UUID.randomUUID().toString(),
        sessionUuid = sessionUuid,
        sequence = sequence,
        post = this,
    )
}

fun List<Post>.toSessionList(sessionUuid: String): List<SessionRow> {
    return mapIndexed { index, post ->
        post.toSession(
            sessionUuid = sessionUuid,
            sequence = index,
        )
    }
}
