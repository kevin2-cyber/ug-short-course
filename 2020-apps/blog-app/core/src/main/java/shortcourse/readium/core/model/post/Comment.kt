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
    tableName = Entities.COMMENTS, foreignKeys = [
        ForeignKey(
            entity = Account::class,
            parentColumns = ["account"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Post::class,
            parentColumns = ["post"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class Comment(
    @PrimaryKey
    override val id: String,
    val account: String,
    val post: String,
    var message: String,
    var timestamp: Long = System.currentTimeMillis()
) : ReadiumModel