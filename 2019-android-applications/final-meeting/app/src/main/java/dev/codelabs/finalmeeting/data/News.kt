package dev.codelabs.finalmeeting.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Sample news data model
 */
@Parcelize
data class News(
    val id: String,
    val title: String,
    val desc: String,
    val url: String,
    val imageUri: String? = null
) : Parcelable