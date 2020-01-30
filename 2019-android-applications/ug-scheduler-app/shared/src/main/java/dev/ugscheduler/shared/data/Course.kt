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
@Entity(tableName = Constants.COURSES)
data class Course(
    @PrimaryKey
    val id: String,
    var name: String,
    var desc: String,
    var icon: String?,
    var session: String,
    var maxStudents: Int,
    var facilitator: String,
    var startDate: Long,
    var endDate: Long
) : Parcelable {
    @Ignore
    constructor() : this(
        UUID.randomUUID().toString(), "", "", "","", 10,"", System.currentTimeMillis(),
        System.currentTimeMillis()
    )
}

fun Course.isInProgress(): Boolean = System.currentTimeMillis() in (startDate + 1) until endDate