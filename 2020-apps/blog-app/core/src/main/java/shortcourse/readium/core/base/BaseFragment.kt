package shortcourse.readium.core.base

import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

/**
 * Base class for all fragments
 */
open class BaseFragment : Fragment() {
    private val job = Job()
    protected val ioScope = CoroutineScope(Dispatchers.IO)
    protected val uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onDestroyView() {
        job.cancel()
        super.onDestroyView()
    }
}