package io.shortcourse.firebasetestapplication

import android.content.Context
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast

/**
 * Validates [views] from a login / sign up form
 */
fun validateForm(vararg views: EditText): Boolean {
    var valid = false
    for (input in views) {
        valid = input.text.isNotEmpty() && input.text.length > 4
    }
    return valid
}

fun validateEmail(email: EditText): Boolean = Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()

fun debugLog(msg: Any?) = println("Debugger -> ${msg.toString()}")

fun Context.toast(msg: Any?) = Toast.makeText(this, msg.toString(), Toast.LENGTH_LONG).show()