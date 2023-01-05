package com.uragiristereo.mejiboard.domain.entity.booru.tag

data class TagsResult(
    val data: List<Tag> = emptyList(),
    val errorMessage: String? = null,
)
