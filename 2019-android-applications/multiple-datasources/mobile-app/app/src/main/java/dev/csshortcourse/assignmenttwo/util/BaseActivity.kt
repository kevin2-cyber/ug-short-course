package dev.csshortcourse.assignmenttwo.util

import androidx.appcompat.app.AppCompatActivity
import dev.csshortcourse.assignmenttwo.preferences.AppPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

/**
 * Base class for all activities
 */
open class BaseActivity : AppCompatActivity() {
    // Coroutines
    val ioScope = CoroutineScope(IO)
    val uiScope = CoroutineScope(Main)

    // Get the Shared Preferences instance
    val prefs: AppPreferences by lazy { AppPreferences.get(applicationContext) }

}