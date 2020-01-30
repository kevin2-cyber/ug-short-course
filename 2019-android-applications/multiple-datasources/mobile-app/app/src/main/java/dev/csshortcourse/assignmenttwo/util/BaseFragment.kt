package dev.csshortcourse.assignmenttwo.util

import androidx.fragment.app.Fragment
import dev.csshortcourse.assignmenttwo.preferences.AppPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

open class BaseFragment : Fragment() {
    // Coroutines
    val ioScope = CoroutineScope(Dispatchers.IO)
    val uiScope = CoroutineScope(Dispatchers.Main)

    // Get the Shared Preferences instance
    val prefs: AppPreferences by lazy { AppPreferences.get(requireContext()) }
}