/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.shared.util.prefs

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.ugscheduler.shared.util.Constants

/**
 * SharedPreferences for users
 */
class UserSharedPreferences private constructor(context: Context) {
    private val prefs by lazy {
        context.getSharedPreferences(
            Constants.USER_SHARED_PREFS,
            Context.MODE_PRIVATE
        )
    }

    private val _liveLoginState = MutableLiveData<Boolean>()
    val liveLoginState: LiveData<Boolean> = _liveLoginState
    var isLoggedIn: Boolean = false

    val uid: String
        get() = prefs.getString("user_id", "") ?: ""

    fun login(uid: String?) {
        isLoggedIn = !uid.isNullOrEmpty()

        prefs.edit {
            putString("user_id", uid)
            apply()
        }
        _liveLoginState.value = !uid.isNullOrEmpty()
    }

    fun logout() {
        isLoggedIn = false
        prefs.edit().clear().apply()
        _liveLoginState.value = false
    }

    init {
        isLoggedIn = !prefs.getString("user_id", null).isNullOrEmpty()
        _liveLoginState.value = isLoggedIn
    }

    companion object {
        @Volatile
        private var instance: UserSharedPreferences? = null

        /**
         * Creates singleton instance of [UserSharedPreferences]
         */
        fun getInstance(context: Context): UserSharedPreferences = instance ?: synchronized(this) {
            instance ?: UserSharedPreferences(context).also { instance = it }
        }
    }
}