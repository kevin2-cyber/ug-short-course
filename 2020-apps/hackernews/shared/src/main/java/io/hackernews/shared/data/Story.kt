package io.hackernews.shared.data

import android.os.Parcelable
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.hackernews.shared.Constants
import kotlinx.android.parcel.Parcelize

@Entity(tableName = Constants.STORIES)
@Parcelize
data class Story(
    @PrimaryKey val id: Long,
    val title: String,
    val url: String?,
    val score: Int,
    @Nullable
    val kids: List<Int>?,
    val time: Long,
    val type: String
) : Parcelable {

    companion object {
        val DIFF_UTIL: DiffUtil.ItemCallback<Story> =
            object : DiffUtil.ItemCallback<Story>() {
                override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                    return oldItem == newItem
                }
            }
    }

}