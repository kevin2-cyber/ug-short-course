package shortcourse.readium.core.util

import timber.log.Timber

/**
 * For debugging
 */
fun debugger(message: Any?) = Timber.d("Readium => %s", message.toString())