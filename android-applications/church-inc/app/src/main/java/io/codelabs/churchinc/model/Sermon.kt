package io.codelabs.churchinc.model

import kotlinx.android.parcel.Parcelize

@Parcelize
data class Sermon(
    override val key: String,
    override var name: String,
    var keyText: String,
    override var avatar: String? = null
) : SearchableDataModel {

    constructor() : this("", "", "")
}