package shortcourse.readium.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.viewModel
import shortcourse.readium.core.util.debugger
import shortcourse.readium.core.util.showSnackbar
import shortcourse.readium.core.viewmodel.AccountViewModel
import shortcourse.readium.core.viewmodel.CommentViewModel
import shortcourse.readium.core.viewmodel.PostViewModel
import shortcourse.readium.databinding.FragmentPostBinding


/**
 * A simple [Fragment] subclass.
 */
class PostFragment : Fragment() {
    private lateinit var binding: FragmentPostBinding
    private val args by navArgs<PostFragmentArgs>()

    private val postViewModel by viewModel<PostViewModel>()
    private val commentsViewModel by viewModel<CommentViewModel>()
    private val accountViewModel by viewModel<AccountViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (args.post == null) {
            findNavController().popBackStack()
            return
        }

        // Observe account owner
        accountViewModel.currentUser.observe(viewLifecycleOwner, Observer { account ->
            binding.run {
                author = account
                executePendingBindings()
            }
            if (account != null) {
                binding.authorAvatar.setOnClickListener {
                    findNavController().navigate(
                        PostFragmentDirections.actionNavPostToNavSettings(
                            account
                        )
                    )
                }
            }
        })

        // Observe comments
        commentsViewModel.comments.observe(viewLifecycleOwner, Observer { comments ->
            debugger("Comments for blog -> ${comments?.map { it.id }}")
            // TODO: 2/22/2020 Show comments for post
        })

        // Related posts
        postViewModel.allPosts.observe(viewLifecycleOwner, Observer { relatedPosts ->
            debugger("Related -> ${relatedPosts?.map { it.id }}")
            // TODO: 2/22/2020 Show related posts
        })

        // View binding
        binding.run {
            post = args.post
            commentsViewModel.getCommentsForPost(args.post!!.id)
            accountViewModel.getUserById(args.post!!.authorId)
            postViewModel.getPostForAuthor(args.post!!.authorId)
            commentOnPost.setOnClickListener {
                findNavController().navigate(PostFragmentDirections.actionNavPostToNavComment())
            }
            moreOptions.setOnClickListener {
                root.showSnackbar("This option is currently unavailable")
            }
        }

    }

}