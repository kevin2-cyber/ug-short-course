package io.hackernews.shared

import timber.log.Timber

/**
 * For debugging
 * @param message Message to be displayed in the console
 */
fun debugger(message: Any?) = Timber.d("HackerNews => %s", message.toString())