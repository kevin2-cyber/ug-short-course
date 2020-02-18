package shortcourse.readium.core.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import shortcourse.readium.core.database.ReadiumDatabase
import shortcourse.readium.core.model.account.Account
import shortcourse.readium.core.model.post.Comment
import shortcourse.readium.core.model.post.Post
import shortcourse.readium.core.util.debugger
import java.io.InputStreamReader

/**
 * Deserializes & loads sample data into the database
 */
class SampleDataWorker(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {
    private val database: ReadiumDatabase by lazy { ReadiumDatabase.getInstance(context) }
    private val accountDao by lazy { database.accountDao() }
    private val postDao by lazy { database.postDao() }
    private val commentDao by lazy { database.commentDao() }

    override suspend fun doWork(): Result {
        withContext(Dispatchers.IO) {
            try {
                val gson = Gson()
                val accounts = gson.fromJson<List<Account>>(
                    InputStreamReader(
                        applicationContext.assets.open("accounts.json")
                    ), object : TypeToken<List<Account>>() {}.type
                ).toMutableList()

                val posts = gson.fromJson<List<Post>>(
                    InputStreamReader(
                        applicationContext.assets.open("posts.json")
                    ), object : TypeToken<List<Post>>() {}.type
                ).toMutableList()

                val comments = gson.fromJson<List<Comment>>(
                    InputStreamReader(
                        applicationContext.assets.open("comments.json")
                    ), object : TypeToken<List<Comment>>() {}.type
                ).toMutableList()

                // Insert data into database
                accountDao.insertAll(accounts)
                postDao.insertAll(posts)
                commentDao.insertAll(comments)
            } catch (e: Exception) {
                debugger(e.localizedMessage)
            }
        }
        return Result.success()
    }

}