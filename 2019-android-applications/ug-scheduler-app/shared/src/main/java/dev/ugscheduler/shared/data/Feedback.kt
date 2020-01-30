/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.shared.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import dev.ugscheduler.shared.util.Constants
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = Constants.FEEDBACK)
data class Feedback(
    @PrimaryKey
    val id: String,
    var message: String,
    var sender: String
) : Parcelable {

    @Ignore
    constructor() : this(UUID.randomUUID().toString(), "", "")
}