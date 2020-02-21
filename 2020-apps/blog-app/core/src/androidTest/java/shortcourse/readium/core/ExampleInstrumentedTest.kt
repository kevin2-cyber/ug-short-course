package shortcourse.readium.core

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.junit.Test
import org.junit.runner.RunWith
import shortcourse.readium.core.database.ReadiumDatabase
import shortcourse.readium.core.repository.PostRepositoryImpl
import shortcourse.readium.core.storage.AccountPrefs
import shortcourse.readium.core.util.debugger

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    private val ir = InstrumentationRegistry.getInstrumentation()

    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private val prefs by lazy { AccountPrefs.getInstance(ir.context) }

    private val database by lazy { ReadiumDatabase.getInstance(ir.context) }
    private val postDao by lazy { database.postDao() }
    private val commentDao by lazy { database.commentDao() }
    private val accountDao by lazy { database.accountDao() }

    private val ioScope = CoroutineScope(Dispatchers.IO + Job())

    @FlowPreview
    @ExperimentalCoroutinesApi
    private val postRepo by lazy {
        PostRepositoryImpl(
            firestore,
            prefs,
            postDao,
            commentDao,
            accountDao
        )
    }

    @InternalCoroutinesApi
    @FlowPreview
    @ExperimentalCoroutinesApi
    @Test
    fun testPostRepository() {
        ioScope.launch {
            postRepo.getAllPosts().collect {
                val data = it.dataOrNull()
                debugger(data?.map { post -> post.id })
            }
        }
    }



}