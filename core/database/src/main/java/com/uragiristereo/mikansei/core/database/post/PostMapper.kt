package com.uragiristereo.mikansei.core.database.post

import com.uragiristereo.mikansei.core.model.danbooru.Post

fun Post.toPostRow(): PostRow {
    return PostRow(
        id = id,
        data = this,
    )
}
