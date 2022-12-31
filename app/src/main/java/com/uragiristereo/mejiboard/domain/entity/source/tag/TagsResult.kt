package com.uragiristereo.mejiboard.domain.entity.source.tag

data class TagsResult(
    val data: List<Tag> = emptyList(),
    val errorMessage: String? = null,
)
