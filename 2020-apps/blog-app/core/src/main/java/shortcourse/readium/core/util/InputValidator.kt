package shortcourse.readium.core.util

import android.util.Patterns
import android.widget.EditText

/**
 * Validate email & password fields
 */
object InputValidator {

    // Matches email pattern -> mail@domain.com
    fun validateEmail(email: EditText): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email.resolveText).matches()

    // Has at least 6 characters
    fun validatePassword(password: EditText): Boolean = password.resolveText.length >= 6

}