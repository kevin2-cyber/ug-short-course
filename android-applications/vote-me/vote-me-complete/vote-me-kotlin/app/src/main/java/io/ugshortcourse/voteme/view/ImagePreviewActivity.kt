package io.ugshortcourse.voteme.view

import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.load.engine.DiskCacheStrategy
import io.ugshortcourse.voteme.R
import io.ugshortcourse.voteme.core.VoteMeBaseActivity
import io.ugshortcourse.voteme.core.glide.GlideApp
import kotlinx.android.synthetic.main.activity_image_preview.*

class ImagePreviewActivity(override val layoutId: Int = R.layout.activity_image_preview) : VoteMeBaseActivity() {


    override fun onViewCreated(instanceState: Bundle?, intent: Intent) {
        //Set support for toolbar menu
        setSupportActionBar(toolbar)

        //Set action for navigation icon on the toolbar
        toolbar.setNavigationOnClickListener { onBackPressed() }

        if (intent.hasExtra(EXTRA_IMAGE_URL)) {

            //Load image into preview source
            GlideApp.with(this)
                .asBitmap()
                .load(intent.getStringExtra(EXTRA_IMAGE_URL))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(android.R.color.black)
                .error(android.R.color.black)
                .fallback(android.R.color.black)
                .into(preview_image)
        }
    }

    companion object {
        const val EXTRA_IMAGE_URL = "EXTRA_IMAGE_URL"
    }
}