package com.uragiristereo.mikansei.core.database.dao.user

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("select * from users")
    fun getAll(): Flow<List<UserRow>>

    @Query("select * from users where id = :id")
    fun get(id: Int): Flow<UserRow>

    @Query("select * from users where isActive = 1 limit 1")
    fun getActive(): Flow<UserRow>

    @Query("select * from users where isActive = 0")
    fun getInactive(): Flow<List<UserRow>>

    @Query("select count(*) from users where id = :id")
    suspend fun isUserExists(id: Int): Boolean

    @Query("update users set isActive = 1 where id = :id")
    suspend fun activate(id: Int)

    @Query("update users set isActive = 0 where id in (:ids)")
    suspend fun deactivate(ids: List<Int>)

    @Insert
    suspend fun add(user: UserRow)

    @Update
    suspend fun update(user: UserRow)

    @Delete
    suspend fun delete(user: UserRow)
}
