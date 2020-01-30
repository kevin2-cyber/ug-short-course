/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package io.codelabs.githubrepo.shared.util

import android.content.Context
import android.content.Intent
import android.widget.Toast
import io.codelabs.githubrepo.shared.core.base.BaseActivity

// For debugging
fun Any.debugger(msg: Any?) =
    //Timber.d("${this::class.java.simpleName} ==> GitRepo ${msg.toString()}")
    println("${this::class.java.simpleName} ==> GitRepo ${msg.toString()}")

// Handles intent in a graceful manner
fun BaseActivity.intentTo(target: Class<out BaseActivity>, finished: Boolean = false) {
    startActivity(Intent(this, target))
    if (finished) finishAfterTransition()
}

fun Context.toast(msg: Any?) = Toast.makeText(this, msg.toString(), Toast.LENGTH_LONG).show()