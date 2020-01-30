package dev.csshortcourse.assignment.model

/**
 * Data class for all [User]s
 */
data class User(
    val uid: String,
    val displayName: String,
    val email: String,
    val createdAt: Long = System.currentTimeMillis()
) {
    // no-argument constructor for Firebase
    constructor() : this("", "", "")
}