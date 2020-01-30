package dev.csshortcourse.assignmenttwo.util

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf

fun debugger(msg: Any?) = println("Assignment => ${msg.toString()}")

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun BaseActivity.moveTo(
    target: Class<out BaseActivity>,
    finish: Boolean = false,
    data: Bundle = bundleOf()
) {
    startActivity(Intent(this, target).apply { putExtras(data) })
    if (finish) finishAfterTransition()
}