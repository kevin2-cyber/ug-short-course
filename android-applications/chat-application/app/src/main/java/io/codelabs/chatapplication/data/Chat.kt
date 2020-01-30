package io.codelabs.chatapplication.data

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Chat(
    override var key: String,
    @SerializedName("recipient")
    override var name: String,
    var createdAt: Long,
    var avatar: String? = "",
    var blocked: Boolean = false,
    var favorite: Boolean = false
) : BaseDataModel {

    constructor() : this("", "", System.currentTimeMillis())
}