package io.codelabs.churchinc.util

import android.util.Patterns
import android.view.View
import android.widget.EditText

object InputValidator {

    /**
     * Toggle enabled state of each [views] passed in
     */
    fun toggleFields(state: Boolean, vararg views: View) {
        for (view in views) {
            view.isEnabled = state
        }
    }

    /**
     * Validate email fields
     */
    fun validateEmails(view: EditText): Boolean {
        val email = view.text.toString()
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validateField(view: EditText): Boolean {
        val field = view.text.toString()
        return field.isNotEmpty() && field.length > 5
    }


}