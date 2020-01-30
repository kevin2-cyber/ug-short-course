package developer.quicknotes.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * Entity
 *
 * This annotation marks a class as a database row.
 * For each Entity, a database table is created to hold the items.
 * The Entity class must be referenced in the Database#entities array.
 * Each field of the Entity (and its super class)
 * is persisted in the database unless it is denoted otherwise (see Entity docs for details).
 */
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var message: String,
    val timestamp: Long = System.currentTimeMillis()
) {
    @Ignore
    constructor(message: String) : this(0, message)

}