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
import shortcourse.readium.core.database.CommentDao
import shortcourse.readium.core.model.post.Comment

interface CommentRepository : Repository {

    suspend fun getAllComments(postId: String): Flow<StoreResponse<MutableList<Comment>>>

    suspend fun getSingleComment(id: String): Flow<StoreResponse<Comment>>

}

class CommentRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val commentDao: CommentDao
) : CommentRepository {
    private val ioScope = CoroutineScope(Dispatchers.IO)

    @ExperimentalCoroutinesApi
    @FlowPreview
    override suspend fun getAllComments(postId: String): Flow<StoreResponse<MutableList<Comment>>> =
        StoreBuilder.from<String, MutableList<Comment>> {
            commentDao.getCommentForPost(it)
        }.scope(ioScope).build().stream(StoreRequest.cached(postId, true))

    @ExperimentalCoroutinesApi
    @FlowPreview
    override suspend fun getSingleComment(id: String): Flow<StoreResponse<Comment>> =
        StoreBuilder.from<String, Comment> {
            commentDao.getCommentById(it)
        }.scope(ioScope).build().stream(StoreRequest.cached(id, true))

}