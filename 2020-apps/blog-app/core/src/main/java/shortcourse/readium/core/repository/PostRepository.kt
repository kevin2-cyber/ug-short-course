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

    suspend fun getPostsForAuthor(authorId: String): Flow<StoreResponse<MutableList<Post>>>

    suspend fun getPostForAuthor(authorId: String, postId: String): Flow<StoreResponse<Post?>>

    suspend fun getPostByTags(tags: MutableList<String>): Flow<StoreResponse<MutableList<Post>>>

    suspend fun getCommentsForPost(postId: String): Flow<StoreResponse<MutableList<Comment>>>
}

@ExperimentalCoroutinesApi
@FlowPreview
class PostRepositoryImpl(
        private val firestore: FirebaseFirestore,
        private val prefs: AccountPrefs,
        private val postDao: PostDao,
        private val commentDao: CommentDao,
        private val accountDao: AccountDao
) : PostRepository {

    private val ioScope = CoroutineScope(Dispatchers.IO)

    override suspend fun getAllPosts(): Flow<StoreResponse<MutableList<Post>>> {
        val store = StoreBuilder.from<Datasource, MutableList<Post>> {
            postDao.getAllPosts()
        }.scope(ioScope).build()

        return store.stream(StoreRequest.cached(Datasource.POSTS, true))
    }


    override suspend fun getPostsForAuthor(authorId: String): Flow<StoreResponse<MutableList<Post>>> {
        return StoreBuilder.from<String, MutableList<Post>> {
            postDao.getPostsForAuthor(it)
        }.scope(ioScope).build().stream(StoreRequest.cached(authorId, true))
    }

    override suspend fun getPostForAuthor(authorId: String, postId: String):
            Flow<StoreResponse<Post?>> {
        return StoreBuilder.from<String, Post> {
            postDao.getPostForAuthor(it, postId)
        }.scope(ioScope).build().stream(StoreRequest.cached(authorId, true))
    }

    // TODO: 2/21/2020 Get post by tags
    override suspend fun getPostByTags(tags: MutableList<String>): Flow<StoreResponse<MutableList<Post>>> {
        return StoreBuilder.from<MutableList<String>, MutableList<Post>> { posts ->
            postDao.getPostsByTags(posts)/*.apply {
                map {
                    it.map { post ->
                        if (post.tags.containsAll(tags)) post
                        else null
                    }
                }
            }*/
        }.scope(ioScope).build().stream(StoreRequest.cached(tags, true))
    }

    override suspend fun getCommentsForPost(postId: String): Flow<StoreResponse<MutableList<Comment>>> {
        return StoreBuilder.from<String, MutableList<Comment>> {
            commentDao.getCommentForPost(postId)
        }.scope(ioScope).build().stream(StoreRequest.cached(postId, true))
    }

}