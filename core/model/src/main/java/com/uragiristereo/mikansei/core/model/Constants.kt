package com.uragiristereo.mikansei.core.model

object Constants {
    const val STATE_KEY_INITIALIZED = "main.initialized"
    const val STATE_KEY_POSTS_SELECTED_BOORU = "posts.selected.booru"
    const val STATE_KEY_POSTS_SESSION_ID = "posts.session.id"
    const val API_DELAY = 150L
    const val USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:95.0) Gecko/20100101 Firefox/95.0"
    const val KEY_LOAD_MORE_PROGRESS = "posts.post.load.more"
    const val POSTS_PER_PAGE = 100

    val SUPPORTED_TYPES_IMAGE = listOf("jpg", "jpeg", "png", "gif")
    val SUPPORTED_TYPES_VIDEO = listOf("webm", "mp4")
    val SUPPORTED_TYPES_ANIMATED = SUPPORTED_TYPES_VIDEO + listOf("gif")
}
