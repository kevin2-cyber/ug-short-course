package io.codelabs.chatapplication.room

import androidx.lifecycle.LiveData
import androidx.room.*
import io.codelabs.chatapplication.data.User

@Dao
interface ChatAppDao {
    /**
     * Create a new [User]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createUser(user: User)

    /**
     * Get a list of all [User]s
     */
    @Query("SELECT * FROM user ORDER BY timestamp ASC")
    fun getAllUsers(): LiveData<MutableList<User>>

    /**
     * Get a single [User] by [key]
     */
    @Query("SELECT * FROM user WHERE user.`key` = :key")
    fun getCurrentUser(key: String): LiveData<User>

    /**
     * Delete a single [User]
     */
    @Delete
    fun deleteUser(user: User)

    /**
     * Delete all [User]s
     */
    @Delete
    fun deleteAllUser(vararg user: User)

    /**
     * Update [user]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateUser(user: User)
}