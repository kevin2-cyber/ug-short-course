package shortcourse.readium.core.base

import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

/**
 * Base class for all activities
 */
open class BaseActivity : AppCompatActivity() {
    private val job = Job()
    protected val ioScope = CoroutineScope(Dispatchers.IO)
    protected val uiScope = CoroutineScope(Dispatchers.Main + job)


    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}