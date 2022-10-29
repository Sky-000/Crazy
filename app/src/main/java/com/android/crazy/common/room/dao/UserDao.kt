package com.android.crazy.common.room.dao

import androidx.room.*
import com.android.crazy.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBatch(users: List<User>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(user: User)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateBatch(users: List<User>)

    @Delete
    suspend fun delete(user: User)

    @Delete
    suspend fun deleteBatch(users: List<User>)

    @Query("SELECT * FROM user")
    fun getAll(): Flow<List<User>>

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUser(id: Int): Flow<User>
}