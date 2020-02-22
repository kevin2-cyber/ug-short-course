package shortcourse.readium.core.model.post

import android.content.Context
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import io.codelabs.dateformatter.DateFormatter
import kotlinx.android.parcel.Parcelize
import shortcourse.readium.core.model.ReadiumModel
import shortcourse.readium.core.model.account.Account
import shortcourse.readium.core.util.Entities

@Parcelize
@Entity(
    tableName = Entities.COMMENTS, foreignKeys = [
        ForeignKey(
            entity = Account::class,
            parentColumns = ["id"],
            childColumns = ["account"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Post::class,
            parentColumns = ["id"],
            childColumns = ["post"],
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
) : ReadiumModel {

    @Ignore
    constructor() : this("", "", "", "")

    @Ignore
    fun getTime(context: Context): String = DateFormatter(context).getTimestamp(timestamp)

    companion object {
        val DIFF_UTIL: DiffUtil.ItemCallback<Comment> = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean =
                oldItem.id == newItem.id && oldItem.account == newItem.account
        }
    }
}