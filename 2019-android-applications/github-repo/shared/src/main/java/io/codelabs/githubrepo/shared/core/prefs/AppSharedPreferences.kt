/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package io.codelabs.githubrepo.shared.core.prefs

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.codelabs.githubrepo.shared.util.Constants

/**
 * Shared preferences implementation to store user's login uid and token
 */
class AppSharedPreferences private constructor(context: Context) {

    // Shared preferences initialisation
    private val prefs by lazy {
        context.getSharedPreferences(
            Constants.APP_PREFS,
            Context.MODE_PRIVATE
        )
    }

    // Live state listener
    private val _liveLoginStatus = MutableLiveData<Boolean>()
    val liveLoginStatus: LiveData<Boolean> = _liveLoginStatus
    val isLoggedIn: Boolean get() = _liveLoginStatus.value ?: false

    // Variable to store user id
    private var uid: String? = null
        get() = prefs.getString("key_uid", null)
        set(value) {
            field = value
            prefs.edit {
                putString("key_uid", value)
                apply()
            }
            _liveLoginStatus.value = !value.isNullOrEmpty()
        }

    // variable for user's login token
    var token: String? = null
        get() = prefs.getString("key_token", null)
        set(value) {
            field = value
            prefs.edit {
                putString("key_token", value)
                apply()
            }
        }

    init {
        uid = prefs.getString("key_uid", null)
        token = prefs.getString("key_token", null)
        _liveLoginStatus.value = !uid.isNullOrEmpty()
    }

    // Login user
    fun login(uid: String?, token: String?) {
        this.uid = uid
        this.token = token
    }

    // Logout user
    fun logout() {
        prefs.edit().clear().apply()
        _liveLoginStatus.value = false
    }

    companion object {
        @Volatile
        private var instance: AppSharedPreferences? = null

        /**
         * Static entry point for the class
         */
        fun get(context: Context): AppSharedPreferences = instance
            ?: synchronized(this) {
                instance ?: AppSharedPreferences(context).also { instance = it }
            }
    }
}