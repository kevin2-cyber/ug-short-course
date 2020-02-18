package shortcourse.readium.core.model

import android.os.Parcelable

/**
 * Base data model for all data classes
 */
interface ReadiumModel : Parcelable {
    val id: String
}