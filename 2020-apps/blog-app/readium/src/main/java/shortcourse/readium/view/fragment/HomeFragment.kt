package shortcourse.readium.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.get
import shortcourse.readium.core.base.BaseFragment
import shortcourse.readium.core.database.ReadiumDatabase
import shortcourse.readium.core.model.post.Post
import shortcourse.readium.core.util.debugger
import shortcourse.readium.databinding.FragmentHomeBinding
import shortcourse.readium.view.recyclerview.PostsAdapter

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : BaseFragment(), PostsAdapter.OnPostItemListener {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Setup adapter
        val adapter = PostsAdapter(this@HomeFragment, get())
        binding.run {
            postsList.adapter = adapter
            executePendingBindings()
        }

        val db = get<ReadiumDatabase>()
        lifecycleScope.launchWhenStarted {
            db.postDao().getAllPosts().collect { posts ->
                adapter.submitList(posts)
            }

            db.accountDao().getAllAccounts().collect { accounts ->
                debugger("Accounts found as -> ${accounts.map { it.id }}")
            }
        }

    }

    override fun onClick(item: Post) {
        findNavController().navigate(HomeFragmentDirections.actionNavHomeToNavPost(item))
    }

    override fun onCommentClick(item: Post) {
        debugger("comment clicked")
    }

    override fun onVoteClick(item: Post) {
        debugger("vote clicked")
    }

    override fun onReportClick(item: Post) {
        debugger("report clicked")
    }

}