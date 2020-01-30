package io.codelabs.ugcloudchat.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * Chat data model
 */
@Entity(tableName = "chats")
data class Chat(
    @PrimaryKey(autoGenerate = false)
    var key: String,
    val message: String,
    val sender: String,
    val recipient: String,
    val timestamp: Long = System.currentTimeMillis()
) {

    @Ignore
    constructor() : this("", "", "", "")

    @Ignore
    constructor(recipient: String, message: String) : this("", message, "", recipient)
}