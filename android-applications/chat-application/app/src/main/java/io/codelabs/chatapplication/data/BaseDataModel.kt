package io.codelabs.chatapplication.data

import android.os.Parcelable

/**
 * Base data model class
 */
interface BaseDataModel : Parcelable {

    var key: String

    var name: String
}