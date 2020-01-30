package io.shortcourse.truchat.util

import android.view.View
import android.widget.EditText

object Constants {
    const val DB_REF = "truchat_shortcourse_db"
    const val SHARED_PREF = "truchat_shared_pref"

}

object InputValidator {

    fun validate(vararg v: EditText): Boolean {
        var valid: Boolean = false
        for (view in v) {
            valid = view.text.toString().isNotEmpty() && view.text.length > 4
        }
        return valid
    }
}

fun View.toggleVisibility(state: Boolean = false) {
    if (state) visibility = View.VISIBLE else View.GONE
}

fun toggleView(state: Boolean = false,vararg views: View){

}