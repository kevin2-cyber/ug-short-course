package dev.csshortcourse.assignmenttwo.view.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.transform.CircleCropTransformation
import dev.csshortcourse.assignmenttwo.R
import dev.csshortcourse.assignmenttwo.databinding.ItemUserBinding
import dev.csshortcourse.assignmenttwo.model.User
import dev.csshortcourse.assignmenttwo.util.BaseActivity

interface UserClickListener {
    fun onClick(user: User)
}

class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: User) {
        binding.userAvatar.load(user.avatar) {
            placeholder(R.drawable.ic_avatar)
            error(R.drawable.ic_avatar)
            transformations(CircleCropTransformation())
        }
        binding.userEmail.text = user.id
        binding.userName.text = user.name
    }
}

class UserAdapter(private val ctx: BaseActivity, private val listener: UserClickListener) :
    ListAdapter<User, UserViewHolder>(DIFF_UTIL) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding =
            ItemUserBinding.bind(ctx.layoutInflater.inflate(R.layout.item_user, parent, false))
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
        holder.itemView.setOnClickListener {
            listener.onClick(user)
        }
    }

    companion object {
        private val DIFF_UTIL: DiffUtil.ItemCallback<User> =
            object : DiffUtil.ItemCallback<User>() {
                override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
                    oldItem == newItem
            }
    }
}