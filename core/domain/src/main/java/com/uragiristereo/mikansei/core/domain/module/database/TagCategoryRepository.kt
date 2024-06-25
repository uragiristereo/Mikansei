package com.uragiristereo.mikansei.core.domain.module.database

import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Tag
import kotlinx.coroutines.flow.Flow

interface TagCategoryRepository {
    fun getCategories(tags: List<String>): Flow<Map<String, Tag.Category>>

    suspend fun update(tags: Map<String, Tag.Category>)
}
