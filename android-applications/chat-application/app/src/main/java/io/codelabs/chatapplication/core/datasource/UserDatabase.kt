package io.codelabs.chatapplication.core.datasource

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import io.codelabs.chatapplication.core.ChatApplication

class UserDatabase private constructor(application: ChatApplication) {
    private val prefs: SharedPreferences = application.getSharedPreferences(PREFS_CHAT_APP, Context.MODE_PRIVATE)

    var isLoggedIn: Boolean = false
    var key: String? = null
        set(value) {
            field = value
            isLoggedIn = true
            prefs.edit {
                putString(KEY_USER_UID, value)
                apply()
            }
        }

    init {
        key = prefs.getString(KEY_USER_UID, "")
        isLoggedIn = !key.isNullOrEmpty()
        if (isLoggedIn) key = prefs.getString(KEY_USER_UID, "")
    }


    companion object {
        private const val PREFS_CHAT_APP = "chat-prefs"
        private const val KEY_USER_UID = "KEY_USER_UID"
        private var instance: UserDatabase? = null
        private val lock = Any()

        fun getInstance(app: ChatApplication): UserDatabase {
            if (instance == null) {
                synchronized(lock) {
                    instance = UserDatabase(app)
                }
            }
            return instance!!
        }
    }
}