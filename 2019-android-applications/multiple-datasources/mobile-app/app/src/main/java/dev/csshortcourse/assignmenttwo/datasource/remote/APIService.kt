package dev.csshortcourse.assignmenttwo.datasource.remote

import dev.csshortcourse.assignmenttwo.model.Chat
import dev.csshortcourse.assignmenttwo.model.User
import retrofit2.http.*

interface APIService {
    @POST("/users/{id}")
    suspend fun getUser(@Path("id") id: String): User?

    @GET("/users")
    suspend fun getAllUsers(): MutableList<User>

    @GET("/chats")
    suspend fun getMyChats(@Query("sender") sender: String?, @Query("recipient") recipient: String): MutableList<Chat>

//    @POST("/chats/new")
//    suspend fun addMessage(@Body chat: Chat): Void

    @GET("/chats/new")
    suspend fun addMessage(
        @Query("id") id: String,
        @Query("sender") sender: String,
        @Query("recipient") recipient: String,
        @Query("message") message: String,
        @Query("timestamp") timestamp: Long = System.currentTimeMillis()
    ): Void


    @POST("/users/new/multi")
    suspend fun addUsers(@Body users: MutableList<User>): Void

    @DELETE("/chats/{id}")
    suspend fun deleteMessage(@Path("id") id: String): Void

//    @POST("/auth")
//    suspend fun login(@Body user: User): User?

    @GET("/auth")
    suspend fun login(
        @Query("id") id: String,
        @Query("name") name: String,
        @Query("avatar") avatar: String? = null
    ): User?
}

/**
 * Send request body of type:
 *  req.body.sender & req.body.recipient
 */
data class ChatRequest(val sender: String?, val recipient: String)