package io.codelabs.ugcloudchat.model.preferences

import android.content.Context
import androidx.core.content.edit
import io.codelabs.ugcloudchat.util.UGCloudChatConstants

class UserSharedPreferences private constructor(context: Context) {

    /**
     * Create [android.content.SharedPreferences] instance
     */
    private val prefs by lazy {
        context.getSharedPreferences(
            UGCloudChatConstants.DEFAULT_PREFS,
            Context.MODE_PRIVATE
        )
    }

    val isLoggedIn: Boolean get() = !uid.isNullOrEmpty()

    var uid: String? = null
        get() = prefs.getString(Keys.KEY_UID, null)
        set(value) {
            prefs.edit {
                putString(Keys.KEY_UID, value)
                apply()
            }
            field = value
        }

    companion object {

        object Keys {
            const val KEY_UID = "key_uid"
        }

        @Volatile
        private var instance: UserSharedPreferences? = null

        fun getInstance(context: Context): UserSharedPreferences = instance ?: synchronized(this) {
            instance ?: UserSharedPreferences(context).also { instance = it }
        }
    }

}