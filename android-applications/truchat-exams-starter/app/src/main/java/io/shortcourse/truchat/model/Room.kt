package io.shortcourse.truchat.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "rooms")
data class Room(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        var name: String
) : Parcelable {

    @Ignore
    constructor(name: String) : this(0, name)
}