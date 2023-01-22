package com.uragiristereo.mejiboard.core.database.dao.saved_searches

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedSearchesDao {
    @Query(value = "SELECT * FROM saved_searches")
    fun getAll(): Flow<List<SavedSearchTableItem>>

    @Query(value = "SELECT COUNT(id) FROM saved_searches")
    fun getCount(): Flow<Int>

    @Insert
    suspend fun insert(item: SavedSearchTableItem)

    @Update
    suspend fun update(item: SavedSearchTableItem)

    @Delete
    suspend fun deleteItems(items: List<SavedSearchTableItem>)
}
