package dev.csshortcourse.assignmenttwo.datasource.remote

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.csshortcourse.assignmenttwo.datasource.DataSource
import dev.csshortcourse.assignmenttwo.model.Chat
import dev.csshortcourse.assignmenttwo.model.User
import dev.csshortcourse.assignmenttwo.preferences.AppPreferences
import dev.csshortcourse.assignmenttwo.util.WorkState
import dev.csshortcourse.assignmenttwo.util.debugger
import dev.csshortcourse.assignmenttwo.viewmodel.repository.Callback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Remote data source
 */
class RemoteDataSource constructor(app: Application) : DataSource {
    private val prefs: AppPreferences by lazy { AppPreferences.get(app) }
    private val apiService: APIService by lazy { APIBuilder.getService() }

    override suspend fun getUser(id: String): User? {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getUser(id)
            } catch (e: Exception) {
                debugger(e.localizedMessage)
                null
            }
        }
    }

    override suspend fun login(user: User, callback: Callback<User>): User? {
        callback(WorkState.STARTED, null)
        return withContext(Dispatchers.IO) {
            try {
                val newUser = apiService.login(user.id, user.name, user.avatar)
                if (newUser == null) {
                    callback(WorkState.ERROR, null)
                } else callback(WorkState.COMPLETED, newUser)
                newUser
            } catch (e: Exception) {
                callback(WorkState.ERROR, null)
                debugger(e.localizedMessage)
                null
            }
        }
    }

    override suspend fun getAllUsers(): MutableList<User> {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getAllUsers()
            } catch (e: Exception) {
                debugger(e.localizedMessage)
                mutableListOf<User>()
            }
        }
    }

    override suspend fun getMyChats(recipient: String): LiveData<MutableList<Chat>> {
        return withContext(Dispatchers.IO) {
            val liveChats = MutableLiveData<MutableList<Chat>>()
            try {
                liveChats.postValue(apiService.getMyChats(prefs.userId, recipient))
            } catch (e: Exception) {
                debugger(e.localizedMessage)
                mutableListOf<Chat>()
            }
            return@withContext liveChats
        }
    }

    override suspend fun addMessage(chat: Chat) {
        withContext(Dispatchers.IO) {
            try {
                apiService.addMessage(chat.id, chat.sender, chat.recipient, chat.message)
                getMyChats(chat.recipient)
                null
            } catch (e: Exception) {
                debugger(e.localizedMessage)
            }
        }
    }

    suspend fun addUsers(users: MutableList<User>) {
        withContext(Dispatchers.IO) {
            try {
                apiService.addUsers(users)
            } catch (e: Exception) {
                debugger(e.localizedMessage)
            }
        }
    }

    suspend fun deleteMessage(chatId: String) {
        withContext(Dispatchers.IO) {
            try {
                apiService.deleteMessage(chatId)
            } catch (e: Exception) {
                debugger(e.localizedMessage)
            }
        }
    }
}