package io.codelabs.chatapplication.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import io.codelabs.chatapplication.R

/**
 * prints a message of [Any] object to the console
 */
fun debugLog(msg: Any?) = println("ChatApp -> ${msg.toString()}")

fun Context.toast(message: Any? = "", @StringRes resId: Int = R.string.empty_text) = Toast.makeText(
    this,
    if (message.toString().isEmpty()) getString(resId) else message.toString(),
    Toast.LENGTH_SHORT
).show()

fun Activity.intentTo(target: Class<out BaseActivity>, isFinished: Boolean = false) {
    startActivity(Intent(this, target))
    if (isFinished) finishAfterTransition()
}

fun Activity.intentTo(target: Class<out BaseActivity>, bundle: Bundle) {
    val intent = Intent(this, target)
    intent.putExtras(bundle)
    startActivity(intent)
}