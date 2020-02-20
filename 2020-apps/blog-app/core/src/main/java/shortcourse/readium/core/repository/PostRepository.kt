package shortcourse.readium.core.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import shortcourse.readium.core.database.AccountDao
import shortcourse.readium.core.database.CommentDao
import shortcourse.readium.core.database.PostDao
import shortcourse.readium.core.model.post.Comment
import shortcourse.readium.core.model.post.Post
import shortcourse.readium.core.storage.AccountPrefs

interface PostRepository : Repository {
    suspend fun getAllPosts(): Flow<MutableList<Post>>

    suspend fun getPostsForAuthor(authorId: String): Flow<MutableList<Post>>

    suspend fun getPostForAuthor(authorId: String): Flow<Post?>

    suspend fun getPostByTags(tags: MutableList<String>): Flow<MutableList<Post>>

    suspend fun getCommentsForPost(postId: String): Flow<MutableList<Comment>>
}

class PostRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val prefs: AccountPrefs,
    private val postDao: PostDao,
    private val commentDao: CommentDao,
    private val accountDao: AccountDao
) : PostRepository {
    override suspend fun getAllPosts(): Flow<MutableList<Post>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getPostsForAuthor(authorId: String): Flow<MutableList<Post>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getPostForAuthor(authorId: String): Flow<Post?> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getPostByTags(tags: MutableList<String>): Flow<MutableList<Post>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getCommentsForPost(postId: String): Flow<MutableList<Comment>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}