package io.codelabs.churchinc.util

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import io.codelabs.churchinc.R
import io.codelabs.churchinc.core.RootActivity

/**
 * For debugging only
 */
fun debugLog(msg: Any?) = println("ChurchInc -> ${msg.toString()}")

fun Context.toast(message: String?) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

/**
 * Add new [Fragment]s
 */
fun RootActivity.addFragment(fragment: Fragment, layoutId: Int = R.id.frame_home) =
    supportFragmentManager.beginTransaction().replace(layoutId, fragment, fragment::class.java.simpleName).commit()