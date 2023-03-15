package com.uragiristereo.mejiboard.domain.entity.tag

data class TagAutoComplete(
    val value: String,
    val category: TagCategory,
    val postCount: Long,
    val postCountFormatted: String,
    val antecedent: String?,
)
