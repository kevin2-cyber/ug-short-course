package developer.quicknotes.extensions

import android.content.Context
import android.widget.Toast

/**
 * For debugging any object
 */
fun Any?.print() = println("QuickNotes: ${this.toString()}")

/**
 * Utility function for creating a new toast message
 */
fun Context.toast(message: Any?) =
    Toast.makeText(this, message.toString(), Toast.LENGTH_SHORT).show()