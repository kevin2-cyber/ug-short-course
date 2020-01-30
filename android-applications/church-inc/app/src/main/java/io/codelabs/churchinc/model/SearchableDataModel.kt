package io.codelabs.churchinc.model

import android.os.Parcelable

interface SearchableDataModel : Parcelable {

    val key: String

    var name: String

    // ONLY FOR USERS
    var avatar: String?

}