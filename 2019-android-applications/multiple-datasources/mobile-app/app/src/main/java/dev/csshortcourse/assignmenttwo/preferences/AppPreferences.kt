package dev.csshortcourse.assignmenttwo.preferences

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.csshortcourse.assignmenttwo.util.Utils

/**
 * Persists user's login state to the local device storage
 */
class AppPreferences private constructor(context: Context) {
    // Shared preference instance
    private val prefs = context.getSharedPreferences(Utils.APP_PREFS, Context.MODE_PRIVATE)

    // Live login state listener instance
    private val _liveLoginState = MutableLiveData<Boolean>()

    /**
     * Store login credentials
     */
    fun login(uid: String?) {
        // Store user's id in share preferences
        prefs.edit {
            putString(USER_ID, uid)
            apply()
        }
        _liveLoginState.postValue(!uid.isNullOrEmpty())
    }

    /**
     * Clear login credentials
     */
    fun logout() {
        // Clear shared preferences
        prefs.edit {
            clear()
            apply()
        }
        _liveLoginState.postValue(false)
    }

    companion object {
        private const val USER_ID = "user_id"
        /**
         * Creating a singleton for our preference instance
         */
        @Volatile
        private var instance: AppPreferences? = null

        fun get(context: Context): AppPreferences = instance ?: synchronized(this) {
            instance ?: AppPreferences(context).also { instance = it }
        }
    }

    /**
     * Getter variable for our private [_liveLoginState]
     */
    val liveLoginState: LiveData<Boolean> get() = _liveLoginState

    val userId: String? get() = prefs.getString(USER_ID, null)

    init {
        // Set default value when class is created for the first time
        // Will be the if value is not null & not empty as well
        _liveLoginState.postValue(!prefs.getString(USER_ID, null).isNullOrEmpty())
    }
}