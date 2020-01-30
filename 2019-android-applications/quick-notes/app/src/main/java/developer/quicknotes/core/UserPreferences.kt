package developer.quicknotes.core

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * Stores user's username as a file in shared preferences
 */
class UserPreferences constructor(context: Context) {

    // Create a shared preference
    private val prefs: SharedPreferences =
        context.getSharedPreferences("notes_user_prefs", Context.MODE_PRIVATE)


    /**
     * Add new username
     */
    fun addUsername(username: String?) {
        prefs.edit {
            putString("key_username", username)
            apply()
        }
    }

    /**
     * Get username
     */
    fun getUsername(): String? = prefs.getString("key_username", null)
}