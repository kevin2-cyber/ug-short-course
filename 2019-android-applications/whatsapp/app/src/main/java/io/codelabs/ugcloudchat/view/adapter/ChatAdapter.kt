package io.codelabs.ugcloudchat.view.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.codelabs.ugcloudchat.R
import io.codelabs.ugcloudchat.model.Chat
import io.codelabs.ugcloudchat.model.preferences.UserSharedPreferences
import io.codelabs.ugcloudchat.util.layoutInflater
import kotlinx.android.synthetic.main.item_message_sender.view.*

class ChatAdapter(
    private val context: Context,
    private val listener: OnConversationClickListener,
    private val prefs: UserSharedPreferences
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val dataset = mutableListOf<Chat>()


    override fun getItemViewType(position: Int): Int {
        return when {
            dataset.isNotEmpty() -> when {
                // For sender
                dataset[position].sender == prefs.uid -> R.layout.item_message_sender

                // For recipient
                else -> R.layout.item_message_recipient
            }

            // For empty messages
            else -> R.layout.item_message_empty
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {

            R.layout.item_message_sender -> ConversationViewHolder(
                context.layoutInflater.inflate(
                    viewType,
                    parent,
                    false
                )
            )
            R.layout.item_message_recipient -> ConversationViewHolder(
                context.layoutInflater.inflate(
                    viewType,
                    parent,
                    false
                )
            )
            R.layout.item_message_empty -> EmptyViewHolder(
                context.layoutInflater.inflate(
                    viewType,
                    parent,
                    false
                )
            )

            else -> throw IllegalStateException("Cannot create an instance of this viewholder")
        }
    }

    // Get the number of items to show in the recyclerview
    override fun getItemCount(): Int = if (dataset.isEmpty()) 1 else dataset.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_message_sender -> bindSenderUI(
                dataset[position],
                holder as ConversationViewHolder,
                position
            )

            R.layout.item_message_recipient -> bindRecipientUI(
                dataset[position],
                holder as ConversationViewHolder,
                position
            )

            R.layout.item_message_empty -> bindEmptyView(holder as EmptyViewHolder)
        }
    }

    private fun bindEmptyView(holder: EmptyViewHolder) {
        // todo: bind the empty view
    }

    private fun bindSenderUI(
        chat: Chat,
        holder: ConversationViewHolder,
        position: Int
    ) {
        holder.v.sender_text_message.text = chat.message.trim()
        // todo: add support for user's profile image in the future

        // Click action
        holder.v.setOnClickListener { listener.onClick(chat, position) }
    }

    private fun bindRecipientUI(
        chat: Chat,
        holder: ConversationViewHolder,
        position: Int
    ) {
        // todo: bind the recipient for each message
    }

    fun addNewMessages(chats: MutableList<Chat>?) {
        // Return when the new messages list is null or empty
        if (chats.isNullOrEmpty()) return

        // Add new messages to the existing list
        this.dataset.clear()
        this.dataset.addAll(chats)
        notifyDataSetChanged()
    }


    /**
     * ViewHolder for each line of conversation in the chat
     */
    class ConversationViewHolder(val v: View) : RecyclerView.ViewHolder(v)

    /**
     * Shows an empty view for no messages
     */
    class EmptyViewHolder(val v: View) : RecyclerView.ViewHolder(v)
}