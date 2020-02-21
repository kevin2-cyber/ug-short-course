package shortcourse.readium.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import shortcourse.readium.core.model.post.Post
import shortcourse.readium.core.storage.AccountPrefs
import shortcourse.readium.core.util.debugger
import shortcourse.readium.core.viewmodel.AccountViewModel
import shortcourse.readium.core.viewmodel.PostViewModel
import shortcourse.readium.databinding.FragmentSettingsBinding
import shortcourse.readium.view.recyclerview.PostsAdapter


/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val accountViewModel by viewModel<AccountViewModel>()
    private val postViewModel by viewModel<PostViewModel>()

    // Account Prefs instance
    private val prefs by inject<AccountPrefs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Check user login state
        if (!prefs.isLoggedIn) {
            // toast("You are not logged in yet")
            findNavController().navigate(SettingsFragmentDirections.actionNavSettingsToNavAuth())
            return
        }

        // Observe current user
        accountViewModel.currentUser.observe(viewLifecycleOwner, Observer {
            debugger("Current user -> $it")
            binding.account = it
            // binding.isFollowing = false // TODO: 2/21/2020 Add following state
            if (it != null && it.id.isNotEmpty()) {
                postViewModel.getPostForAuthor(it.id)
            }
        })

        // Observe user's posts
        val adapter = PostsAdapter(object : PostsAdapter.OnPostItemListener {
            override fun onClick(item: Post) {

            }

            override fun onCommentClick(item: Post) {

            }

            override fun onVoteClick(item: Post) {

            }

            override fun onReportClick(item: Post) {

            }
        }, get())
        binding.run {
            profileToolbar.setNavigationOnClickListener { findNavController().popBackStack() }
            authorBlogs.adapter = adapter
            follow.setOnClickListener {
                // FIXME: 2/21/2020 Allow user to follow / un-follow an author
            }
        }

        /*postViewModel.allPosts.observe(viewLifecycleOwner, Observer {
            binding.hasBlog = it != null && it.isNotEmpty()
            if (it != null) {
                debugger(it.map { post -> post.id })
                adapter.submitList(it)
            }
        })*/

    }

}