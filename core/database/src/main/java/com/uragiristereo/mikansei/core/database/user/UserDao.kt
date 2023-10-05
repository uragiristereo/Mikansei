package com.uragiristereo.mikansei.core.database.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("select * from users")
    fun getAll(): Flow<List<UserRow>>

    @Query("select * from users where id = :id")
    fun get(id: Int): Flow<UserRow>

    @Query("select * from users where is_active = 1 limit 1")
    fun getActive(): Flow<UserRow>

    @Query("select * from users where is_active = 0")
    fun getInactive(): Flow<List<UserRow>>

    @Query("select count(*) from users where id = :id")
    suspend fun isUserExists(id: Int): Boolean

    @Query("update users set is_active = 1 where id = :id")
    suspend fun activate(id: Int)

    @Query("update users set is_active = 0 where id <> :activeId")
    suspend fun deactivate(activeId: Int)

    @Transaction
    suspend fun switchActiveUser(id: Int) {
        activate(id)
        deactivate(activeId = id)
    }

    @Insert
    suspend fun add(user: UserRow)

    @Update
    suspend fun update(user: UserRow)

    @Delete
    suspend fun delete(user: UserRow)
}
