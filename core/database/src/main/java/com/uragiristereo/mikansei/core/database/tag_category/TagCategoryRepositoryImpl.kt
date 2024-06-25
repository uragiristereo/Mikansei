package com.uragiristereo.mikansei.core.database.tag_category

import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Tag
import com.uragiristereo.mikansei.core.domain.module.database.TagCategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TagCategoryRepositoryImpl(
    private val tagCategoryDao: TagCategoryDao,
) : TagCategoryRepository {
    override fun getCategories(tags: List<String>): Flow<Map<String, Tag.Category>> {
        return tagCategoryDao.getCategories(tags).map {
            it.associate { item ->
                item.tag to Tag.Category.valueOf(item.category)
            }
        }
    }

    override suspend fun update(tags: Map<String, Tag.Category>) {
        val tagList = tags.map { (tag, category) ->
            TagCategoryRow(
                tag = tag,
                category = category.name,
            )
        }

        tagCategoryDao.update(tagList)
    }
}
