package dev.csshortcourse.assignmenttwo.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.csshortcourse.assignmenttwo.R
import dev.csshortcourse.assignmenttwo.model.Chat
import dev.csshortcourse.assignmenttwo.model.User
import dev.csshortcourse.assignmenttwo.util.BaseActivity
import io.codelabs.dateformatter.DateFormatter
import kotlinx.android.synthetic.main.item_recipient.view.*
import kotlinx.android.synthetic.main.item_sender.view.*

class ConversationViewHolder(val v: View) : RecyclerView.ViewHolder(v)

interface ChatClickListener {
    fun onClick(chat: Chat)
}

class ConversationAdapter(
    private val ctx: BaseActivity,
    private val currentUser: User,
    private val listener: ChatClickListener
) :
    ListAdapter<Chat, ConversationViewHolder>(
        DIFF_UTIL
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        return ConversationViewHolder(ctx.layoutInflater.inflate(viewType, parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).sender == currentUser.id) SENDER else RECIPIENT
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        val chat = getItem(position)
        when (getItemViewType(position)) {
            SENDER -> {
                holder.v.sender_name.text = currentUser.name
                holder.v.sender_message.text = chat.message
                holder.v.sender_timestamp.text =
                    DateFormatter(ctx).getConversationTimestamp(chat.timestamp)
            }

            RECIPIENT -> {
                holder.v.recipient_name.text = currentUser.name
                holder.v.recipient_message.text = chat.message
                holder.v.recipient_timestamp.text =
                    DateFormatter(ctx).getConversationTimestamp(chat.timestamp)
            }
        }

        holder.v.setOnClickListener { listener.onClick(chat) }
    }

    companion object {
        private const val SENDER = R.layout.item_sender
        private const val RECIPIENT = R.layout.item_recipient

        private val DIFF_UTIL: DiffUtil.ItemCallback<Chat> =
            object : DiffUtil.ItemCallback<Chat>() {
                override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean =
                    oldItem == newItem
            }
    }
}