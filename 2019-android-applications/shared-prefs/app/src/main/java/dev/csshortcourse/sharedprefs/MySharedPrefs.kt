package dev.csshortcourse.sharedprefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class MySharedPrefs constructor(context: Context) {
    /**
     * Create shared preference variable
     */
    private val prefs by lazy {
        context.getSharedPreferences(
            "my_shared_prefs",
            Context.MODE_PRIVATE
        )
    }

    /**
     * Store value
     */
    fun save(item: String?) {
        debugger("Saving your item")
        prefs.edit {
            putString(KEY_ITEM, item)
            apply()
        }
    }

    /**
     * Retrieve value
     */
    fun retrieve(): String? {
        debugger("retrieving your item")
        return prefs.getString(KEY_ITEM, null)
    }

    /**
     * Clear all values stored
     */
    fun clear() {
        debugger("Clearing your items")
        prefs.edit {
            clear()
            apply()
        }
    }

    private val listener: SharedPreferences.OnSharedPreferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == KEY_ITEM) {
                debugger("Value has been updated")
            }
        }

    init {
        prefs.registerOnSharedPreferenceChangeListener(listener)
    }

    companion object {
        private const val KEY_ITEM = "item_key"
    }
}