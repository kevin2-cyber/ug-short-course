package io.codelabs.ugcloudchat.model.database

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import io.codelabs.ugcloudchat.model.Chat
import io.codelabs.ugcloudchat.model.WhatsappUser

@Dao
interface UGCloudChatDao {

    // region USERS
    /**
     * Get a single list of all the users
     */
    @Query("SELECT * FROM users ORDER BY timestamp DESC")
    fun getAllUsers(): MutableList<WhatsappUser>


    /**
     * Get all user asynchronously
     */
    @Query("SELECT * FROM users ORDER BY timestamp DESC")
    fun getAllUsersAsync(): LiveData<MutableList<WhatsappUser>>

    /**
     * Get a user by [uid]
     */
    @Query("SELECT * FROM users WHERE uid = :uid")
    fun getUserByUID(uid: String): LiveData<WhatsappUser>

    /**
     * Get a user by [id]
     */
    @Query("SELECT * FROM users WHERE id = :id")
    fun getUserById(id: Long): LiveData<WhatsappUser>

    /**
     * Add a list of users
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUsers(users: MutableList<WhatsappUser>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addSingleUser(user: WhatsappUser)

    /**
     * Remove users from the database
     */
    @Delete
    fun removeUser(vararg users: WhatsappUser)

    /**
     * Update a list of users
     */
    @Update
    fun updateUsers(vararg users: WhatsappUser)

    /**
     * Gets the currently logged in user
     */
    @Query("SELECT * FROM users WHERE uid = :uid LIMIT 1")
    fun getCurrentUser(uid: String?): LiveData<WhatsappUser>

    /**
     * Store the current user
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setCurrentUser(user: WhatsappUser)
    // endregion USERS

    // region CHATS
    /**
     * Gets all conversations between [sender] & [recipient]
     */
    @Query("SELECT * FROM chats WHERE sender = :sender AND recipient = :recipient ORDER BY timestamp DESC")
    fun getChatsBetween(
        sender: String?,
        recipient: String?
    ): LiveData<MutableList<Chat>>

    /**
     * Add chats to the database locally
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addChats(chats: MutableList<Chat>)

    @Delete
    fun deleteChat(chat: Chat)
    // endregion CHATS

}