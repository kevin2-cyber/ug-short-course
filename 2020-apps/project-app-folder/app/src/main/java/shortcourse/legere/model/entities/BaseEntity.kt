package shortcourse.legere.model.entities

/**
 * Base class for all data models
 * Contains shared properties between those subclasses
 */
interface BaseEntity {
    val id: String  // Id field
    val timestamp: Long // Timestamp field
}