/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.shared.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.ugscheduler.shared.util.Constants
import kotlinx.android.parcel.Parcelize

/**
 * News data model
 */
@Parcelize
@Entity(tableName = Constants.NEWS)
data class News(
    @PrimaryKey
    val id: String,
    val title: String,
    val desc: String,
    val author: String?,
    val url: String?,
    val timestamp: Long = System.currentTimeMillis()
) : Parcelable