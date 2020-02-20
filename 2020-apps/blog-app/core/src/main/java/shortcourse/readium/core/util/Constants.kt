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

enum class Datasource(val label: String) {
    ACCOUNTS("accounts"), POSTS("posts"), COMMENTS("comments")
}

object DatabaseUtil {
    const val NAME = "readium.db"
    const val VERSION = 2
}

object FirebaseUtil {
    const val BUCKET = "readium"
}

object StorageUtil {
    const val ACCOUNT_PREFS = "account-prefs"
    const val ONBOARDING_PREFS = "onboarding-prefs"

    object Keys {
        const val ID = "account_id"
        const val ROLES = "account_roles"
        const val ONBOARDING_STATE = "onboarding_state"
    }
}