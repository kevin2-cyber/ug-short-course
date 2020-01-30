/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.shared.data

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import dev.ugscheduler.shared.util.Constants
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * For [Anonymous] users
 */
@Parcelize
@Entity(tableName = Constants.ANONYMOUS)
data class Anonymous constructor(
    @PrimaryKey
    override val id: String,
    override val timestamp: Long
) : BaseUser {

    @Ignore
    constructor() : this(UUID.randomUUID().toString(), System.currentTimeMillis())
}

/**
 * [Student]s
 */
@Entity(tableName = Constants.STUDENTS)
@Parcelize
data class Student constructor(
    @PrimaryKey
    @NonNull
    override val id: String,
    override val timestamp: Long,
    val email: String,
    var phone: String?,
    var avatar: String?,
    var dob: String?,
    var fullName: String?,
    var residence: String?,
    var address: String?,
    var organisation: String?,
    var position: String?,
    var eduBackground: String?/*,
    @Ignore
    var prevCourses: MutableList<String> = mutableListOf()*/
) : BaseUser {

    @Ignore
    constructor() : this(
        UUID.randomUUID().toString(),
        System.currentTimeMillis(),
        "",
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )
}

/**
 * [Facilitator]s
 */
@Entity(tableName = Constants.FACILITATORS)
@Parcelize
data class Facilitator constructor(
    @PrimaryKey
    override val id: String,
    override val timestamp: Long = System.currentTimeMillis(),
    var avatar: String?,
    var email: String,
    var fullName: String?,
    var rating: Double,
    var phone: String?/*,
    @Ignore
    var courses: MutableList<String> = mutableListOf()*/
) : BaseUser {

    @Ignore
    constructor() : this(
        UUID.randomUUID().toString(),
        System.currentTimeMillis(),
        null,
        "",
        null,
        1.0,
        null
    )
}
