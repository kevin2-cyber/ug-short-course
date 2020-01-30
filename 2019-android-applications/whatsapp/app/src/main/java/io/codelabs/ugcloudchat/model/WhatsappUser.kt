package io.codelabs.ugcloudchat.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 * User data model
 */
@Parcelize
@Entity(tableName = "users")
data class WhatsappUser(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val uid: String,
    val phone: String?,
    var displayName: String? = null,
    var photoUri: String? = null,
    var thumbNailUri: String? = null,
    var timestamp: Long = System.currentTimeMillis()
) : Parcelable {

    /**
     * Needed by the Firestore SDK for serialization
     */
    @Ignore
    constructor() : this(0L, "", "")

    @Ignore
    constructor(uid: String, phone: String) : this(0L, uid, phone)

}