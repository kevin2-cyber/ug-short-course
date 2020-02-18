package shortcourse.readium.util

import timber.log.Timber

fun debugger(message: Any?) = Timber.d("Readium => ${message.toString()}")