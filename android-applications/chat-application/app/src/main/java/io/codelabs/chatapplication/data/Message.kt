package io.codelabs.chatapplication.data

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Message(
    override var key: String,
    @SerializedName("message")
    override var name: String,/*message*/
    var timestamp: Long = System.currentTimeMillis(),
    var read: Boolean = false,
    var type: String = Message.TYPE_TEXT
) : BaseDataModel {

    constructor() : this("", "")

    companion object {
        const val TYPE_AUDIO = "audio"
        const val TYPE_IMAGE = "image"
        const val TYPE_VIDEO = "video"
        const val TYPE_TEXT = "text"
    }
}