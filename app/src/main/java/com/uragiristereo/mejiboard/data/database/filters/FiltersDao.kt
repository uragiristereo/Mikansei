package com.uragiristereo.mejiboard.data.database.filters

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FiltersDao {
    @Query(value = "SELECT * FROM filters")
    suspend fun getAll(): Flow<List<FilterTableItem>>

    @Query(value = "SELECT tag FROM filters WHERE enabled = 1")
    suspend fun getEnabledFilters(): List<String>

    @Query(value = "SELECT COUNT(tag) FROM filters WHERE enabled = 1")
    suspend fun getEnabledFiltersCount(): Flow<Int>

    @Insert
    suspend fun insert(items: List<FilterTableItem>)

    @Update
    suspend fun update(item: FilterTableItem)

    @Delete
    suspend fun deleteItems(items: List<FilterTableItem>)
}
