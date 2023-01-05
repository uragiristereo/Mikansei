package com.uragiristereo.mejiboard.domain.entity.booru.post

import androidx.compose.runtime.Stable

@Stable
enum class Rating {
    GENERAL,
    SENSITIVE,
    QUESTIONABLE,
    EXPLICIT,
}
