package io.shortcourse.truchat.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "messages")
data class Message(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        var message: String,
        var sender: String,
        var recipient: String,
        val timestamp: Long = System.currentTimeMillis()
) : Parcelable {

    @Ignore
    constructor() : this(0, "", "", "")
}