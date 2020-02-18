package shortcourse.readium.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import shortcourse.readium.R
import shortcourse.readium.core.database.ReadiumDatabase
import shortcourse.readium.core.util.debugger

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val db = ReadiumDatabase.getInstance(requireContext())
        lifecycleScope.launchWhenStarted {
            db.accountDao().getAllAccounts().collect { accounts ->
                debugger("Accounts found as -> ${accounts.map { it.id }}")
            }

            db.postDao().getAllPosts().collect { posts ->
                debugger("Posts found as -> ${posts.map { it.id }}")

                // Get all comments for post
                posts.forEach { story ->
                    db.commentDao().getCommentForPost(story.id).collect { comments ->
                        debugger("Comments for ${story.id} -> ${comments.size}")
                    }
                }
            }
        }
    }

}