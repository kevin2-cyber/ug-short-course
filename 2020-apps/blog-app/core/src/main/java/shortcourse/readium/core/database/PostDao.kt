package shortcourse.readium.core.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import shortcourse.readium.core.model.post.Post

@Dao
interface PostDao : BaseDao<Post> {

    @Query("select * from posts order by timestamp desc")
    fun getAllPosts(): Flow<MutableList<Post>>
}