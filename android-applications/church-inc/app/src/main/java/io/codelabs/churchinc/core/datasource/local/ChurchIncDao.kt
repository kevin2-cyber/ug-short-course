package io.codelabs.churchinc.core.datasource.local

import androidx.lifecycle.LiveData
import androidx.room.*
import io.codelabs.churchinc.model.User

@Dao
interface ChurchIncDao {

    @Insert
    suspend fun createUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM users WHERE `key` = :key")
    suspend fun getCurrentUser(key: String): User

    @Query("SELECT * FROM users WHERE `key` = :key")
    fun getLiveUser(key: String): LiveData<User>

    @Update
    suspend fun updateUser(user: User)

}