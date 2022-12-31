package com.uragiristereo.mejiboard.domain.entity.source.tag

import androidx.compose.runtime.Stable

@Stable
data class Tag(
    val id: Int,
    val name: String,
    val count: Int,
    val countFmt: String,
    val type: TagType,
)
