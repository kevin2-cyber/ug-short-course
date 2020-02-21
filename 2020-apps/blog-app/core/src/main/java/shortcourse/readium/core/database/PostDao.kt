package shortcourse.readium.core.database

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import shortcourse.readium.core.model.post.Post

@Dao
interface PostDao : BaseDao<Post> {

    @Query("select * from posts order by timestamp desc")
    fun getAllPosts(): Flow<MutableList<Post>>

    @Query("select * from posts where authorId = :authorId order by timestamp desc")
    fun getPostsForAuthor(authorId: String): Flow<MutableList<Post>>

    @Query("select * from posts order by timestamp desc")
    fun getPostsByTags(tags: MutableList<String>): Flow<MutableList<Post>>

    @Query("select * from posts where id = :id order by timestamp desc")
    fun getPostById(id: String): Flow<Post>

    @Query("select * from posts where authorId = :authorId and id = :postId order by timestamp desc")
    fun getPostForAuthor(authorId: String, postId: String): Flow<Post>

    @Query("delete from posts")
    suspend fun deleteAll()

    @Query("delete from posts where id = :id")
    suspend fun delete(id: String)
}