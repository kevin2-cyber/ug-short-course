package shortcourse.readium.core.database

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import shortcourse.readium.core.model.post.Comment

@Dao
interface CommentDao : BaseDao<Comment> {

    @Query("select * from comments where post = :post and account = :author")
    fun getCommentForPostAndAuthor(post: String, author: String): Flow<MutableList<Comment>>

    @Query("select * from comments where post = :post")
    fun getCommentForPost(post: String): Flow<MutableList<Comment>>

}