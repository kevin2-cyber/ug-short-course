package shortcourse.readium.view.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import shortcourse.readium.core.database.AccountDao
import shortcourse.readium.core.model.account.Account
import shortcourse.readium.core.model.post.Post
import shortcourse.readium.databinding.PostItemLayoutBinding

class PostViewHolder(
    private val binding: PostItemLayoutBinding,
    private val _listener: PostsAdapter.OnPostItemListener
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Post, account: Account) {
        binding.run {
            post = item
            author = account
            listener = _listener
            context = root.context
            // executePendingBindings()
        }
    }
}

class PostsAdapter(private val listener: OnPostItemListener, private val dao: AccountDao) :
    ListAdapter<Post, PostViewHolder>(Post.POST_DIFF) {

    interface OnPostItemListener {
        fun onClick(item: Post)

        fun onCommentClick(item: Post)

        fun onVoteClick(item: Post)

        fun onReportClick(item: Post)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            PostItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            listener
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val post = getItem(position)
            dao.getAccount(post.authorId).collect {
                CoroutineScope(Dispatchers.Main).launch {
                    holder.bind(post, it)
                }
            }
        }
    }

}