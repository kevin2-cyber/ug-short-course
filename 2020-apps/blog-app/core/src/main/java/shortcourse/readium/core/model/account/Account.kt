package shortcourse.readium.core.model.account

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import shortcourse.readium.core.model.ReadiumModel
import shortcourse.readium.core.util.Entities
import java.util.*

@Parcelize
@Entity(tableName = Entities.ACCOUNTS)
data class Account(
    @PrimaryKey(autoGenerate = false)
    override val id: String,
    var firstName: String,
    var lastName: String,
    var email: String,
    var avatar: String? = null,
    val creationTime: Long = System.currentTimeMillis(),
    var lastSeen: Long = System.currentTimeMillis(),
    var roles: MutableList<String> = mutableListOf(Entities.Role.GUEST.label)
) : ReadiumModel {

    /**
     * Generates a unique id for user based on their first & last names
     */
    fun generateId(): String {
        val trimmedFN = firstName.toLowerCase(Locale.getDefault())
            .replace(" ", "")
            .replace("@", "")
        val trimmedLN = lastName.toLowerCase(Locale.getDefault())
            .replace(" ", "")
            .replace("@", "")

        // Return trimmed name
        return "@${trimmedFN}_$trimmedLN"
    }
}