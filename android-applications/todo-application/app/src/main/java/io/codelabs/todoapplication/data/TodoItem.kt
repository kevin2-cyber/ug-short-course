package io.codelabs.todoapplication.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "todos")
@Parcelize
data class TodoItem constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var content: String,
    var timestamp: Long = System.currentTimeMillis(),
    var completed: Boolean = false
) : Parcelable {

    // Secondary constructor
    @Ignore
    constructor(content: String) : this(0, content)

}