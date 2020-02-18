package shortcourse.readium.core.model.post

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import shortcourse.readium.core.model.ReadiumModel
import shortcourse.readium.core.model.account.Account
import shortcourse.readium.core.util.Entities

@Parcelize
@Entity(
    tableName = Entities.POSTS, foreignKeys = [
        ForeignKey(
            entity = Account::class,
            parentColumns = ["id"],
            childColumns = ["authorId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class Post(
    @PrimaryKey(autoGenerate = false)
    override val id: String,
    val authorId: String,
    var title: String,
    var description: String,
    var approved: Boolean = false,
    var timestamp: Long = System.currentTimeMillis(),
    var comments: MutableList<String> = mutableListOf(),
    var votes: MutableList<String> = mutableListOf(),
    var images: MutableList<String?> = mutableListOf(),
    var reports: MutableList<Int> = mutableListOf(),
    var tags: MutableList<String> = mutableListOf()
) : ReadiumModel