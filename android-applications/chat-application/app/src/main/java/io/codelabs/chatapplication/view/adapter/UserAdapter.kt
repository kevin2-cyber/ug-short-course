package io.codelabs.chatapplication.view.adapter

import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import io.codelabs.chatapplication.R
import io.codelabs.chatapplication.data.BaseDataModel
import io.codelabs.chatapplication.data.Chat
import io.codelabs.chatapplication.data.User
import io.codelabs.chatapplication.glide.GlideApp
import io.codelabs.chatapplication.util.BaseActivity
import io.codelabs.chatapplication.util.USER_DOC_REF
import io.codelabs.chatapplication.util.intentTo
import io.codelabs.chatapplication.view.PreviewActivity
import kotlinx.android.synthetic.main.item_chat.view.*
import kotlinx.android.synthetic.main.item_empty.view.*

class UserAdapter constructor(private val host: BaseActivity, private val listener: ItemClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val TYPE_EMPTY = -1
        private const val TYPE_DATA = 0
    }

    private val dataset: MutableList<in BaseDataModel> = ArrayList<BaseDataModel>(0)

    override fun getItemViewType(position: Int): Int = if (dataset.isEmpty()) TYPE_EMPTY else TYPE_DATA

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_EMPTY -> EmptyViewHolder(LayoutInflater.from(host).inflate(R.layout.item_empty, parent, false))
            else -> ChatViewHolder(LayoutInflater.from(host).inflate(R.layout.item_chat, parent, false))
        }
    }

    override fun getItemCount(): Int = if (dataset.isEmpty()) 1 else dataset.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_DATA -> {
                val item = dataset[position]

                when (item) {
                    is User -> {
                        if (holder is ChatViewHolder) {
                            val avatar = holder.view.user_avatar
                            val username = holder.view.user_name
                            val lastSeen = holder.view.user_last_seen
                            val status = holder.view.user_status

                            // Load profile image
                            GlideApp.with(avatar.context)
                                .asBitmap()
                                .load(item.profile)
                                .circleCrop()
                                .placeholder(R.drawable.avatar_placeholder)
                                .error(R.drawable.avatar_placeholder)
                                .fallback(R.drawable.avatar_placeholder)
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                .transition(withCrossFade())
                                .into(avatar)

                            // Set other properties
                            username.text = item.name
                            status.text = item.status
                            lastSeen.text = DateUtils.getRelativeTimeSpanString(
                                item.lastSeen,
                                System.currentTimeMillis(),
                                DateUtils.SECOND_IN_MILLIS
                            )

                            avatar.setOnClickListener {
                                val bundle = Bundle(0)
                                bundle.putString(PreviewActivity.EXTRA_URL, item.profile)
                                host.intentTo(PreviewActivity::class.java, bundle)
                            }

                            holder.view.setOnClickListener {
                                listener.onClick(item)
                            }
                        }
                    }

                    is Chat -> {
                        if (holder is ChatViewHolder) {
                            loadChat(holder, item)

                            // Load user's information
                            host.firestore.document(String.format(USER_DOC_REF, item.key))
                                .get()
                                .addOnCompleteListener(host) { task ->
                                    if (task.isSuccessful) {
                                        val user = task.result?.toObject(User::class.java)
                                        if (user != null) loadChat(holder, user)
                                    }
                                }

                        }
                    }
                }
            }

            else -> {
                GlideApp.with(host)
                    .asDrawable()
                    .load(R.drawable.empty_content)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into((holder as EmptyViewHolder).view.empty_view)
            }
        }
    }

    private fun loadChat(holder: ChatViewHolder, item: BaseDataModel) {
        val avatar = holder.view.user_avatar
        val username = holder.view.user_name
        val lastSeen = holder.view.user_last_seen
        val status = holder.view.user_status

        if (item is User) {
            // Load profile image
            GlideApp.with(avatar.context)
                .asBitmap()
                .load(item.profile)
                .circleCrop()
                .placeholder(R.drawable.avatar_placeholder)
                .error(R.drawable.avatar_placeholder)
                .fallback(R.drawable.avatar_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .transition(withCrossFade())
                .into(avatar)

            // Set other properties
            username.text = item.name
            status.text = item.status
            lastSeen.text = DateUtils.getRelativeTimeSpanString(
                item.lastSeen,
                System.currentTimeMillis(),
                DateUtils.SECOND_IN_MILLIS
            )

            avatar.setOnClickListener {
                val bundle = Bundle(0)
                bundle.putString(PreviewActivity.EXTRA_URL, item.profile)
                host.intentTo(PreviewActivity::class.java, bundle)
            }
        } else {
            assert(item is Chat)
            // Load profile image
            GlideApp.with(avatar.context)
                .asBitmap()
                .load((item as Chat).avatar)
                .circleCrop()
                .placeholder(R.drawable.avatar_placeholder)
                .error(R.drawable.avatar_placeholder)
                .fallback(R.drawable.avatar_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .transition(withCrossFade())
                .into(avatar)

            // Set other properties
            username.text = item.name

            avatar.setOnClickListener {
                val bundle = Bundle(0)
                bundle.putString(PreviewActivity.EXTRA_URL, item.avatar)
                host.intentTo(PreviewActivity::class.java, bundle)
            }

            holder.view.setOnClickListener {
                listener.onClick(item)
            }
        }
    }

    fun addData(items: MutableList<out BaseDataModel>) {
        dataset.clear()
        dataset.addAll(items)
        notifyDataSetChanged()
    }

    interface ItemClickListener {

        fun onClick(item: BaseDataModel)

    }
}