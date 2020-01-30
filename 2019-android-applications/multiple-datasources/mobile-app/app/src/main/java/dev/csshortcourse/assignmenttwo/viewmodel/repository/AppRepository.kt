package dev.csshortcourse.assignmenttwo.viewmodel.repository

import android.app.Application
import androidx.lifecycle.LiveData
import dev.csshortcourse.assignmenttwo.datasource.DataSource
import dev.csshortcourse.assignmenttwo.datasource.local.LocalDataSource
import dev.csshortcourse.assignmenttwo.datasource.remote.RemoteDataSource
import dev.csshortcourse.assignmenttwo.model.Chat
import dev.csshortcourse.assignmenttwo.model.User
import dev.csshortcourse.assignmenttwo.preferences.AppPreferences
import dev.csshortcourse.assignmenttwo.util.WorkState
import dev.csshortcourse.assignmenttwo.util.debugger
import dev.csshortcourse.assignmenttwo.viewmodel.AppViewModel

// Callback alias
typealias Callback<O> = (WorkState, O?) -> Unit

/**
 * Base repository class
 * Defines which calls can be made to the data sources
 * Same as [DataSource] interface calls
 */
interface Repository {
    suspend fun getCurrentUser(refresh: Boolean): User?
    suspend fun getMyChats(refresh: Boolean, recipient: String): LiveData<MutableList<Chat>>
    suspend fun getUsers(refresh: Boolean): MutableList<User>
    suspend fun getUser(refresh: Boolean, id: String): User?
    suspend fun addMessage(chat: Chat)
    suspend fun addUsers(users: MutableList<User>)
    suspend fun deleteMessage(chat: Chat)
    suspend fun login(user: User, callback: Callback<User>)
    suspend fun logout(callback: Callback<Void>)
}

/**
 * [Repository] for the [AppViewModel]
 */
class AppRepository private constructor(app: Application) : Repository {
    // Shared preferences
    private val prefs: AppPreferences by lazy { AppPreferences.get(app) }
    // Local data source
    private val localDataSource: LocalDataSource by lazy { LocalDataSource(app) }
    // Remote data source
    private val remoteDataSource: RemoteDataSource by lazy { RemoteDataSource(app) }

    override suspend fun getUsers(refresh: Boolean): MutableList<User> {
        return if (refresh) {
            val allUsers = remoteDataSource.getAllUsers()
            if (allUsers.isEmpty()) {
                localDataSource.getAllUsers()
                    .filter { !prefs.userId.isNullOrEmpty() && it.id != prefs.userId }
                    .toMutableList()
            } else allUsers.filter { !prefs.userId.isNullOrEmpty() && it.id != prefs.userId }.toMutableList()
        } else localDataSource.getAllUsers().filter { !prefs.userId.isNullOrEmpty() && it.id != prefs.userId }.toMutableList()
    }

    override suspend fun getCurrentUser(refresh: Boolean): User? {
        return when {
            prefs.userId.isNullOrEmpty() -> null
            refresh -> remoteDataSource.getUser(prefs.userId!!)
            else -> localDataSource.getUser(prefs.userId!!)
        }
    }

    override suspend fun getMyChats(
        refresh: Boolean,
        recipient: String
    ): LiveData<MutableList<Chat>> {
        return if (refresh) remoteDataSource.getMyChats(recipient)
        else localDataSource.getMyChats(recipient)
    }

    override suspend fun login(user: User, callback: Callback<User>) {
        callback(WorkState.STARTED, null)
        debugger("Sending user to server: $user")
        remoteDataSource.login(user, callback).apply {
            debugger("User from server: $this")
            // Store user's id locally using shared preferences
            prefs.login(this?.id)

            // Save user details into the database
            try {
                localDataSource.userDao.insert(this)
            } catch (e: Exception) {
                debugger(e.localizedMessage)
            }
        }
    }

    override suspend fun logout(callback: Callback<Void>) {
        callback(WorkState.STARTED, null)
        if (!prefs.userId.isNullOrEmpty()) {
            localDataSource.userDao.remove(localDataSource.getUser(prefs.userId!!))
            prefs.logout()
            callback(WorkState.COMPLETED, null)
        } else {
            callback(WorkState.COMPLETED, null)
        }
    }

    override suspend fun addMessage(chat: Chat) {
        localDataSource.addMessage(chat).apply { remoteDataSource.addMessage(chat) }
    }

    // todo: fix this
    override suspend fun getUser(refresh: Boolean, id: String): User? {
        return if (refresh) localDataSource.getUser(id)
        else localDataSource.getUser(id)
    }

    override suspend fun addUsers(users: MutableList<User>) {
        localDataSource.userDao.insertAll(users)
        remoteDataSource.addUsers(users)
    }

    override suspend fun deleteMessage(chat: Chat) {
        localDataSource.chatDao.remove(chat)
        remoteDataSource.deleteMessage(chat.id)
    }

    companion object {
        /**
         * Creating singleton
         */
        @Volatile
        private var instance: AppRepository? = null

        fun get(app: Application): AppRepository = instance ?: synchronized(this) {
            instance ?: AppRepository(app).also { instance = it }
        }
    }

}