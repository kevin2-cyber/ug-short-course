/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.shared.data

import android.os.Parcelable

/**
 * Base interface for all users
 */
interface BaseUser : Parcelable {
    val id: String
    val timestamp: Long
}

fun BaseUser.isSignedIn(): Boolean = id.isNotEmpty()