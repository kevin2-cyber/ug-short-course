/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.shared.util

import android.app.Activity
import android.content.Intent

object ImagePickerHelper {

    // Allow user to pick single or multiple images from the gallery app
    fun getImageFromGallery(host: Activity, requestCode: Int, allowMultiple: Boolean = false) {
        // Create intent
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)

        // Allow user to pick multiple images
        if (allowMultiple) galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

        // Start activity for result
        host.startActivityForResult(
            Intent.createChooser(galleryIntent, "Select image(s) from"),
            requestCode
        )
    }

}