package shortcourse.readium.core.datasource

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import shortcourse.readium.core.database.ReadiumDatabase
import shortcourse.readium.core.model.post.Post
import shortcourse.readium.core.util.Datasource
import shortcourse.readium.core.util.Entities
import shortcourse.readium.core.util.debugger

class RemoteDatasource(
    private val firestore: FirebaseFirestore,
    private val database: ReadiumDatabase
) {
    private val commentDao by lazy { database.commentDao() }
    private val postDao by lazy { database.postDao() }
    private val accountDao by lazy { database.accountDao() }

    suspend fun insertAll(source: Datasource, items: MutableList<Post>) {
        postDao.insertAll(items)
    }

    suspend fun delete(source: Datasource) {
        debugger("delete from source -> ${source.label}")
    }

    suspend fun deleteAll() {
        debugger("deleting all")
    }

    @ExperimentalCoroutinesApi
    fun getAllPosts(source: Datasource): Flow<MutableList<Post>> {
        return flow {
            val result = try {
                withContext(Dispatchers.IO) {
                    Tasks.await(firestore.collection(source.label).get()).toObjects<Post>()
                        .toMutableList()
                }
            } catch (ex: Exception) {
                mutableListOf<Post>()
            }

            emit(result)
        }
    }
}