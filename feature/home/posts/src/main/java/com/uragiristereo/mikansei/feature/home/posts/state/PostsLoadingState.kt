package com.uragiristereo.mikansei.feature.home.posts.state

enum class PostsLoadingState {
    FROM_LOAD,
    FROM_LOAD_MORE,
    FROM_REFRESH,
    FROM_RESTORE_SESSION,
    DISABLED_REFRESHED,
    DISABLED,
}
