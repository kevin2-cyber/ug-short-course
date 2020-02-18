package shortcourse.readium.core.util

/**
 * Table names for the database
 */
object Entities {
    const val ACCOUNTS = "accounts"
    const val POSTS = "posts"
    const val COMMENTS = "comments"

    enum class Role(val label: String) {
        AUTHOR("author"), EDITOR("editor"), GUEST("guest")
    }
}

object Database {
    const val NAME = "readium.db"
    const val VERSION = 1
}