package io.hackernews.shared.base

import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

/**
 * Base class for all Activities
 */
abstract class BaseActivity : AppCompatActivity() {
    private val job = Job()
    protected val ioScope = CoroutineScope(Dispatchers.IO)
    protected val uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}