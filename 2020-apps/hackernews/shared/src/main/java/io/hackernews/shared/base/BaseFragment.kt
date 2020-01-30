package io.hackernews.shared.base

import androidx.fragment.app.Fragment
import io.hackernews.shared.database.HackerNewsDatabase
import io.hackernews.shared.database.StoryDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class BaseFragment : Fragment() {
    private val job = Job()
    protected val ioScope = CoroutineScope(Dispatchers.IO)
    protected val uiScope = CoroutineScope(Dispatchers.Main + job)

    protected val dao: StoryDao by lazy {
        HackerNewsDatabase.getInstance(requireContext()).storyDao()
    }

    override fun onDestroyView() {
        job.cancel()
        super.onDestroyView()
    }

}