package dev.csshortcourse.assignmenttwo.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.csshortcourse.assignmenttwo.util.Utils
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * User data model
 */
@Entity(tableName = Utils.USERS)
@Parcelize
data class User(
    @PrimaryKey
    val id: String,
    val name: String,
    val avatar: String? = null,
    var timestamp: Long = System.currentTimeMillis()
) : Parcelable {
    constructor(name: String) : this(UUID.randomUUID().toString(), name)
}