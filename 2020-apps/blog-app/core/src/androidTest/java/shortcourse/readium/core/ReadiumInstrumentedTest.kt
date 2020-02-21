package shortcourse.readium.core

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
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
class ReadiumInstrumentedTest {
    private val ir = InstrumentationRegistry.getInstrumentation()

    private val prefs by lazy { AccountPrefs.getInstance(ir.context) }

    private val database by lazy { ReadiumDatabase.getInstance(ir.context) }
    private val postDao by lazy { database.postDao() }
    private val commentDao by lazy { database.commentDao() }
    private val accountDao by lazy { database.accountDao() }

    @FlowPreview
    @ExperimentalCoroutinesApi
    private val postRepo by lazy {
        PostRepositoryImpl(
            null,
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
    fun testPostRepository() = runBlocking {
        assert(true)
        postRepo.getAllPosts().collect {
            val data = it.dataOrNull()
            debugger(data?.map { post -> post.id })
        }
    }

}