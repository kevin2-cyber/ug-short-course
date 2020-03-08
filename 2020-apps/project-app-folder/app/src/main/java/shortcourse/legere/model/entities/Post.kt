package shortcourse.legere.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * [Post] data model
 */
@Entity(tableName = "posts")
data class Post(
    @PrimaryKey
    override val id: String,
    override val timestamp: Long,
    val authorId: String,
    val tags: MutableList<String>,
    val images: MutableList<String>,
    var title: String,
    var description: String,
    var comments: Int,
    var votes: Int,
    var reports: Int,
    var approved: Boolean
) : BaseEntity