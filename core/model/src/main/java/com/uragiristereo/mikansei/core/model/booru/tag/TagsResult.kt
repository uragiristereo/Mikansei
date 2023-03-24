package com.uragiristereo.mikansei.core.model.booru.tag

data class TagsResult(
    val data: List<Tag> = emptyList(),
    val errorMessage: String? = null,
)
