package dev.csshortcourse.ugnews.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import dev.csshortcourse.ugnews.R

fun AppCompatActivity.moveTo(
    target: Class<out AppCompatActivity>,
    finish: Boolean = false,
    data: Bundle = bundleOf()
) {
    startActivity(Intent(this, target).apply { putExtras(data) })
    if (finish) finishAfterTransition()
}

fun debugger(msg: Any?) = println("NewsApp => ${msg.toString()}")

fun Context.browse(url: String) {
    val tabIntent = CustomTabsIntent.Builder()
        .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
        .build()
    with(tabIntent) {
        launchUrl(this@browse, url.toUri())
    }
}

val Context.layoutInflater: LayoutInflater get() = LayoutInflater.from(this)

fun ImageView.load(url: String?) {
    GlideApp.with(context)
        .asBitmap()
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .placeholder(R.color.colorPrimary)
        .error(R.color.colorPrimaryDark)
        .transition(withCrossFade())
        .into(this)
}

fun View.gone() {
    visibility = View.GONE
}