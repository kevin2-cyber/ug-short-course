package shortcourse.readium.core.repository

import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import shortcourse.readium.core.database.AccountDao
import shortcourse.readium.core.database.CommentDao
import shortcourse.readium.core.database.PostDao
import shortcourse.readium.core.model.post.Comment
import shortcourse.readium.core.model.post.Post
import shortcourse.readium.core.storage.AccountPrefs
import shortcourse.readium.core.util.Datasource

interface PostRepository : Repository {
    suspend fun getAllPosts(): Flow<StoreResponse<MutableList<Post>>>

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

    private val ioScope = CoroutineScope(Dispatchers.IO)

    @ExperimentalCoroutinesApi
    @FlowPreview
    override suspend fun getAllPosts(): Flow<StoreResponse<MutableList<Post>>> {
        val store = StoreBuilder.from<Datasource, MutableList<Post>> {
            postDao.getAllPosts()
        }.scope(ioScope).build()

        return store.stream(StoreRequest.cached(Datasource.POSTS, true))
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