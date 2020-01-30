package io.codelabs.todoapplication.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.AlarmClock
import android.provider.CalendarContract
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.StringRes
import io.codelabs.todoapplication.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

/**
 * Prints a message of [Any] object to the console
 */
fun debugLog(msg: Any?) = println("Todo Application -> ${msg.toString()}")

fun Context.toast(message: Any? = "", @StringRes resId: Int = R.string.empty_text) = Toast.makeText(
    this,
    if (message.toString().isEmpty()) getString(resId) else message.toString(),
    Toast.LENGTH_LONG
).show()

fun Context.createAlarm(message: String) {
    val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
        putExtra(AlarmClock.EXTRA_MESSAGE, message)
        putExtra(AlarmClock.EXTRA_HOUR, Calendar.HOUR_OF_DAY)
        putExtra(AlarmClock.EXTRA_MINUTES, Calendar.MINUTE)
    }
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    }
}

fun Context.addEvent(
    title: String,
    location: String,
    begin: Long = System.currentTimeMillis(),
    end: Long = System.currentTimeMillis().plus(7200000)
) {
    val intent = Intent(Intent.ACTION_INSERT).apply {
        data = CalendarContract.Events.CONTENT_URI
        putExtra(CalendarContract.Events.TITLE, title)
        putExtra(CalendarContract.Events.EVENT_LOCATION, location)
        putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin)
        putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end)
    }
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    }
}

fun Activity.capturePhoto(code: Int) {
    val intent = Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA)
    if (intent.resolveActivity(packageManager) != null) {
        startActivityForResult(intent, code)
    }
}