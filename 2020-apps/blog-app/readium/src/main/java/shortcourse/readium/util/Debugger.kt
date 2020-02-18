package shortcourse.readium.util

import timber.log.Timber

/**
 * For debugging
 */
fun debugger(message: Any?) = Timber.d("Readium => %s", message.toString())