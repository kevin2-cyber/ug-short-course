package io.codelabs.chatapplication.view

import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import io.codelabs.chatapplication.R
import io.codelabs.chatapplication.glide.GlideApp
import io.codelabs.chatapplication.util.BaseActivity
import kotlinx.android.synthetic.main.activity_preview.*

class PreviewActivity(override val layoutId: Int = R.layout.activity_preview) : BaseActivity() {

    override fun onViewCreated(instanceState: Bundle?, intent: Intent) {
        if (intent.hasExtra(EXTRA_URL)) {
            GlideApp.with(this)
                .asBitmap()
                .load(intent.getStringExtra(EXTRA_URL))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .transition(withCrossFade())
                .placeholder(android.R.color.black)
                .error(android.R.color.black)
                .fallback(android.R.color.black)
                .into(preview_image)
        }
    }

    companion object {
        const val EXTRA_URL = "EXTRA_URL"
    }
}