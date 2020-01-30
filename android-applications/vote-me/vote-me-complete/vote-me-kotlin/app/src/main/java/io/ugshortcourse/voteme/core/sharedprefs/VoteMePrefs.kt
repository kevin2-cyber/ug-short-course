package io.ugshortcourse.voteme.core.sharedprefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import io.ugshortcourse.voteme.core.DEFAULT_SHARED_PREFS
import io.ugshortcourse.voteme.model.VoteMeUser
import javax.inject.Inject

class VoteMePrefs @Inject constructor(private val context: Context) {
    companion object {
        private const val KEY_USER_KEY = "KEY_USER_KEY"
        private const val KEY_USER_NAME = "KEY_USER_NAME"
        private const val KEY_USER_ORG = "KEY_USER_ORG"
        private const val KEY_USER_REGION = "KEY_USER_REGION"
        private const val KEY_USER_TIMESTAMP = "KEY_USER_TIMESTAMP"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(DEFAULT_SHARED_PREFS, Context.MODE_PRIVATE)


    var isLoggedIn: Boolean = false
    private var key: String? = ""
    private var org: String? = ""
    private var fullName: String? = ""
    private var region: String? = ""
    private var timestamp: Long = System.currentTimeMillis()

    var user: VoteMeUser? = null
        set(value) {
            if (field == value) return
            field = value
        }

    init {
        //Init keys
        key = prefs.getString(KEY_USER_KEY, "")
        fullName = prefs.getString(KEY_USER_NAME, "")
        org = prefs.getString(KEY_USER_ORG, "")
        region = prefs.getString(KEY_USER_REGION, "")
        timestamp = prefs.getLong(KEY_USER_TIMESTAMP, 0L)

        //Set login state when user's key is not null or empty
        isLoggedIn = !key.isNullOrEmpty()

        //Get key values
        if (isLoggedIn) {
            key = prefs.getString(KEY_USER_KEY, "")
            fullName = prefs.getString(KEY_USER_NAME, "")
            org = prefs.getString(KEY_USER_ORG, "")
            region = prefs.getString(KEY_USER_REGION, "")
            timestamp = prefs.getLong(KEY_USER_TIMESTAMP, 0L)
        }
    }

    //Login user
    fun login(user: VoteMeUser?) {
        user ?: return

        isLoggedIn = true
        this.user = user
        this.key = user.key
        this.org = user.org
        this.region = user.region
        this.fullName = user.fullName
        this.timestamp = user.timestamp

        prefs.edit {
            putString(KEY_USER_KEY, user.key)
            putString(KEY_USER_NAME, user.fullName)
            putString(KEY_USER_ORG, user.org)
            putString(KEY_USER_REGION, user.region)
            putLong(KEY_USER_TIMESTAMP, user.timestamp)
            apply()
        }
    }

    //Logout user
    fun logout() {
        isLoggedIn = false
        this.user = user
        this.key = ""
        this.org = ""
        this.region = ""
        this.fullName = ""
        this.timestamp = 0L

        prefs.edit {
            putString(KEY_USER_KEY, key)
            putString(KEY_USER_NAME, fullName)
            putString(KEY_USER_ORG, org)
            putString(KEY_USER_REGION, region)
            putLong(KEY_USER_TIMESTAMP, timestamp)
            apply()
        }
    }

}