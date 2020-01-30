package dev.csshortcourse.assignmenttwo.datasource

import androidx.lifecycle.LiveData
import dev.csshortcourse.assignmenttwo.model.Chat
import dev.csshortcourse.assignmenttwo.model.User
import dev.csshortcourse.assignmenttwo.viewmodel.repository.Callback

/**
 * Shows which calls can be made to the respective data sources
 */
interface DataSource {
    suspend fun getUser(id: String): User?
    suspend fun getAllUsers(): MutableList<User>
    suspend fun getMyChats(recipient: String): LiveData<MutableList<Chat>>
    suspend fun addMessage(chat: Chat)
    suspend fun login(user: User, callback: Callback<User>): User?
}