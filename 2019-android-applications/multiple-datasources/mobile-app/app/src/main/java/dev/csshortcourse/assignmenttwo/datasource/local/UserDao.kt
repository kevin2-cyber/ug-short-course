package dev.csshortcourse.assignmenttwo.datasource.local

import androidx.room.Dao
import androidx.room.Query
import dev.csshortcourse.assignmenttwo.model.User

/**
 * Data Access Object for [User]
 */
@Dao
interface UserDao : BaseDao<User> {
     @Query("select * from users where id = :id")
    suspend fun getUser(id: String): User?

     @Query("select * from users order by timestamp desc")
    suspend fun getAllUsers(): MutableList<User>
}