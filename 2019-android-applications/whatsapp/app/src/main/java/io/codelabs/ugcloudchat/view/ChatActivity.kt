package io.codelabs.ugcloudchat.view

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import io.codelabs.ugcloudchat.R
import io.codelabs.ugcloudchat.model.Chat
import io.codelabs.ugcloudchat.model.WhatsappUser
import io.codelabs.ugcloudchat.util.debugThis
import io.codelabs.ugcloudchat.util.toast
import io.codelabs.ugcloudchat.view.adapter.ChatAdapter
import io.codelabs.ugcloudchat.view.adapter.OnConversationClickListener
import io.codelabs.ugcloudchat.viewmodel.ChatViewModel
import kotlinx.android.synthetic.main.activity_chat.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChatActivity : BaseActivity() {

    private val viewModel by viewModel<ChatViewModel>()

    // Store recipient's details
    private var user: WhatsappUser? = null
    private var id: Long? = null

    // Adapter fot displaying messages
    private lateinit var adapter: ChatAdapter


    // Store current user's uid
    private val currentUser: String? by lazy { prefs.uid }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // Setup the recyclerview
        setupRecyclerView()

        if (intent.hasExtra(ID)) {
            debugThis(intent.getLongExtra(ID, -1L))
            debugThis(intent.getParcelableExtra(USER))

            id = intent.getLongExtra(ID, -1L)
            user = intent.getParcelableExtra<WhatsappUser>(USER)

            debugThis("Showing chats with: $user")

            // Fixed error
            // I was loading from the user's id instead of phone number
            if (user != null && !user?.phone.isNullOrEmpty()) {
                viewModel.getOnlineMessagesWith(user?.phone.toString())
                    .observe(this, Observer { allChats ->
                        adapter.addNewMessages(allChats)
                        scrollToBottom()
                    })
            }
        } else {
            toast("You cannot start a conversation with a user that does not exist")
            finish()
        }

    }

    private fun setupRecyclerView() {
        adapter = ChatAdapter(this, object :
            OnConversationClickListener {
            override fun onClick(chat: Chat, position: Int) {
                // todo: add action for click on each message
            }
        }, prefs)

        // A layout manager to show all messages
        messages_list.layoutManager = LinearLayoutManager(this).apply {
            // Allow new items to be added at the bottom of the list rather than the default method where messages
            // are added at the top
            stackFromEnd = true
        }
        messages_list.adapter = adapter
        messages_list.setHasFixedSize(false)
        messages_list.itemAnimator = DefaultItemAnimator()
    }

    /**
     * Send message
     */
    fun sendMessage(view: View?) {

        // get the text from the input field
        val message = message_input.text.toString()

        // Check whether the text is empty or not
        if (message.isEmpty()) {
            toast("Cannot send an empty message")
        } else {
            // Create message
            val chat = Chat(
                "",
                message.trim(), // We trim down the text to remove all trailing whitespaces
                currentUser!!,
                user?.phone ?: user?.id.toString()
            )

            // Send message to the database
            viewModel.sendMessage(chat)

            // Clear the message once it is sent
            message_input.text?.clear()

            // Scroll to the last message in the list
            scrollToBottom()
        }
    }

    private fun scrollToBottom() {
        // Check if there are contents in the list first
        if (adapter.itemCount > 0) messages_list.smoothScrollToPosition(adapter.itemCount.minus(1))
    }

    companion object {
        const val ID = "id"
        const val USER = "user"
    }
}
