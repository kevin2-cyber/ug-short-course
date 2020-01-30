package io.shortcourse.truchat.core.datasource

import androidx.lifecycle.LiveData
import androidx.room.*
import io.shortcourse.truchat.model.Message
import io.shortcourse.truchat.model.Room
import io.shortcourse.truchat.model.User

/**
 * Data Access Object: For communicating with the underlying [Room] database
 */
@Dao
interface TruChatDao {

    @Query("SELECT * FROM rooms ORDER BY name ASC")
    fun getRooms(): LiveData<MutableList<Room>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRoom(vararg rooms: Room)

    @Query("SELECT * FROM rooms WHERE name IS :name")
    fun getRoom(name: String): Room?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(vararg user: User)

    @Query("SELECT * FROM users WHERE id IS :id")
    fun getUser(id: Int): LiveData<User>

    @Query("SELECT * FROM users WHERE username IS :username")
    fun getUser(username: String): LiveData<User>

    @Query("SELECT * FROM users ORDER BY username")
    fun getAllUsers(): LiveData<MutableList<User>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMessage(vararg messages: Message)

    @Query("SELECT * FROM messages WHERE sender IS :sender ORDER BY timestamp DESC")
    fun getMyMessages(sender: String): LiveData<MutableList<Message>>

    @Delete
    fun removeMessage(vararg messages: Message)

}