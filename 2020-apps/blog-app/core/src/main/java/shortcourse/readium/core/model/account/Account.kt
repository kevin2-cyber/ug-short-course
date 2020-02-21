package shortcourse.readium.core.model.account

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import shortcourse.readium.core.model.ReadiumModel
import shortcourse.readium.core.util.Entities
import java.util.*

@Parcelize
@Entity(tableName = Entities.ACCOUNTS)
data class Account(
    @PrimaryKey(autoGenerate = false)
    override val id: String,
    val uid: String? = UUID.randomUUID().toString(),
    var firstName: String,
    var lastName: String,
    var email: String,
    @ColumnInfo(name = "pen_name")
    var penName: String?,
    var avatar: String? = null,
    val creationTime: Long = System.currentTimeMillis(),
    var lastSeen: Long = System.currentTimeMillis(),
    var roles: MutableList<String> = mutableListOf(Entities.Role.GUEST.label)
) : ReadiumModel {

    @Ignore
    constructor() : this("", "", "", "", "", "")

    @IgnoredOnParcel
    @Ignore
    val displayName = "$firstName $lastName"

    companion object {
        /**
         * Generates a unique id for user based on their first & last names
         */
        fun generateId(firstName: String?, lastName: String?): String {
            val trimmedFN = firstName?.toLowerCase(Locale.getDefault())
                ?.replace(" ", "")
                ?.replace("@", "")
            val trimmedLN = lastName?.toLowerCase(Locale.getDefault())
                ?.replace(" ", "")
                ?.replace("@", "")

            // Return trimmed name
            return "@${trimmedFN}_$trimmedLN"
        }
    }
}