package com.uragiristereo.mikansei.feature.home.posts.state

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostsSavedState(
    val loadFromSession: Boolean = false,
    val scrollIndex: Int = 0,
    val scrollOffset: Int = 0,
) : Parcelable
