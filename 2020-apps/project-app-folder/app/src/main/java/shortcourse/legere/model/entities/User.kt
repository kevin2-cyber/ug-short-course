package shortcourse.legere.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * [User] data model
 */
@Entity(tableName = "users")
data class User(
    @PrimaryKey
    override val id: String,
    val email: String,
    override val timestamp: Long = System.currentTimeMillis(),
    var firstName: String? = null,
    var lastName: String? = null,
    var avatar: String? = null,
    val roles: MutableList<String> = mutableListOf(),
    var lastSeen: Long = System.currentTimeMillis()
) : BaseEntity

/**
 * Roles assigned to each user
 */
object Roles {
    const val GUEST = "guest"
    const val READER = "reader"
    const val AUTHOR = "author"
    const val EDITOR = "editor"
}

/*enum class ARole(val type: String) {
    GUEST("guest"),
    AUTHOR("author"),
    READER("reader"),
    EDITOR("editor")
}*/
