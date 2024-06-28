package com.uragiristereo.mikansei.core.database.tag_category

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TagCategoryDao {
    @Query("select * from tag_categories where tag in (:tags)")
    fun getCategories(tags: List<String>): Flow<List<TagCategoryRow>>

    @Upsert
    suspend fun update(tags: List<TagCategoryRow>)
}
