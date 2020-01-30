package dev.csshortcourse.assignmenttwo.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.csshortcourse.assignmenttwo.util.Utils
import kotlinx.android.parcel.Parcelize

/**
 * Chat data model
 */
@Entity(tableName = Utils.CHATS)
@Parcelize
data class Chat(
    @PrimaryKey
    val id: String,
    val sender: String,
    val recipient: String,
    var message: String,
    val timestamp: Long = System.currentTimeMillis()
) : Parcelable