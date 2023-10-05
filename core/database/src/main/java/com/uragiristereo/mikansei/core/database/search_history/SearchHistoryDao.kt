package com.uragiristereo.mikansei.core.database.search_history

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {
    @Query("select * from search_history order by date desc")
    fun getAll(): Flow<List<SearchHistoryRow>>

    @Insert
    suspend fun add(item: SearchHistoryRow)

    @Delete
    suspend fun delete(items: List<SearchHistoryRow>)

    @Query("delete from search_history")
    suspend fun clear()
}
