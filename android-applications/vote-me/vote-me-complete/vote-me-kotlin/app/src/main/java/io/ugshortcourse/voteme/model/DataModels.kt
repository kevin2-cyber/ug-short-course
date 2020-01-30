package io.ugshortcourse.voteme.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Base class for all users [Voter]s & [Candidate]s alike
 */
sealed class VoteMeUser(
    open val key: String,
    open val fullName: String,
    open val region: String,
    open val org: String,
    open val timestamp: Long
) : Parcelable

/**
 * [Voter] data model
 */
@Parcelize
data class Voter(
    override val key: String,
    override val fullName: String,
    override val region: String,
    override val org: String,
    override val timestamp: Long = System.currentTimeMillis(),
    val dues: Long = 0L,
    val voted: Boolean = false
) : VoteMeUser(key, fullName, region, org, timestamp) {

    constructor() : this("", "", "", "")
}

/**
 * [Candidate] data model
 */
@Parcelize
data class Candidate(
    override val key: String,
    override val fullName: String,
    override val region: String,
    override val org: String,
    override val timestamp: Long = System.currentTimeMillis(),
    val image: String,
    val category: String
) : VoteMeUser(key, fullName, region, org, timestamp) {

    constructor() : this("", "", "", "", image = "", category = "")
}

/**
 * [Vote] data model
 */
@Parcelize
data class Vote(
    val key: String,
    val category: String,
    val candidateKey: String,
    val voterKey: String,
    val timestamp: Long = System.currentTimeMillis()
) : Parcelable {

    constructor() : this("", "", "", "")
}