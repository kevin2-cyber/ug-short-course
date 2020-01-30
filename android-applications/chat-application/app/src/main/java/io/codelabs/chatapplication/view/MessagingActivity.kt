package io.codelabs.chatapplication.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.google.firebase.firestore.Query
import io.codelabs.chatapplication.R
import io.codelabs.chatapplication.data.BaseDataModel
import io.codelabs.chatapplication.data.Chat
import io.codelabs.chatapplication.data.Message
import io.codelabs.chatapplication.data.User
import io.codelabs.chatapplication.glide.GlideApp
import io.codelabs.chatapplication.util.*
import io.codelabs.chatapplication.view.adapter.MessagesAdapter
import kotlinx.android.synthetic.main.activity_messaging.*

class MessagingActivity(override val layoutId: Int = R.layout.activity_messaging) : BaseActivity(),
    MessagesAdapter.OnItemClickListener {

    private var isText: Boolean = true
    private var isFavorite: Boolean = false
    private var isBlocked: Boolean = false
    private var key: String = ""

    override fun onViewCreated(instanceState: Bundle?, intent: Intent) {
        setSupportActionBar(toolbar)
        toolbar.title = getString(R.string.empty_text)
        toolbar.setNavigationOnClickListener { finishAfterTransition() }

        if (intent.hasExtra(ProfileActivity.EXTRA_USER)) {
            bindUser(intent.getParcelableExtra<BaseDataModel>(EXTRA_USER))
        } else if (intent.hasExtra(EXTRA_USER_ID)) {
            loadUserById(intent.getStringExtra(EXTRA_USER_ID))
        }
    }

    private fun loadUserById(id: String?) {
        if (id.isNullOrEmpty()) return
        firestore.document(String.format(USER_CHATS_DOC_REF, database.key, id))
            .addSnapshotListener(this@MessagingActivity) { snapshot, exception ->
                if (exception != null) {
                    debugLog(exception.localizedMessage)
                    toast("Cannot load current user's profile")
                    return@addSnapshotListener
                }

                // Get user from snapshot and bind the data to the UI
                val user = snapshot?.toObject(Chat::class.java)
                bindUser(user)
                invalidateOptionsMenu()
            }
    }

    private fun bindUser(user: BaseDataModel?) {
        if (user == null) {
            toast("Cannot load user's profile")
            return
        }

        debugLog(user)

        // Setup user data
        toolbar_title.text = user.name

        GlideApp.with(this)
            .asBitmap()
            .load(if (user is User) user.profile else (user as Chat).avatar)
            .circleCrop()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .placeholder(R.drawable.avatar_placeholder)
            .error(R.drawable.avatar_placeholder)
            .transition(BitmapTransitionOptions.withCrossFade())
            .into(avatar)

        avatar.setOnClickListener {
            val bundle = Bundle(0)
            bundle.putString(PreviewActivity.EXTRA_URL, if (user is User) user.profile else (user as Chat).avatar)
            intentTo(PreviewActivity::class.java, bundle)
        }

        fetchMessages(user.key)

    }

    private fun fetchMessages(key: String) {
        // Set Chat key
        this.key = key


        val adapter = MessagesAdapter(this, this)
        message_grid.adapter = adapter
        val lm = LinearLayoutManager(this)
        lm.stackFromEnd = true
        message_grid.layoutManager = lm
        message_grid.setHasFixedSize(true)
        message_grid.itemAnimator = DefaultItemAnimator()

        firestore.collection(String.format(USER_MESSAGES_DOC_REF, database.key, key))
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener(this@MessagingActivity) { snapshot, exception ->
                if (exception != null) {
                    debugLog(exception.cause)
                    toast("Unable to retrieve messages")
                    return@addSnapshotListener
                }

                // Load message from snapshot and append to UI
                val messages = snapshot?.toObjects(Message::class.java)
                if (messages != null) {
                    adapter.addData(messages.toMutableList())
                    if (adapter.isNotEmpty()) {
                        message_grid.smoothScrollToPosition(adapter.bottom)
                    }
                }

            }

        // Update all dos to reflect read state
        firestore.collection(String.format(USER_MESSAGES_DOC_REF, database.key, key))
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val documents = it.result?.documents
                    documents?.forEach { doc ->
                        doc.reference.update(
                            mapOf<String, Any?>(
                                "read" to true
                            )
                        ).addOnCompleteListener { }.addOnFailureListener { }
                    }
                }
            }


        send_message_button.setOnClickListener {
            val message = message_view.text.toString()
            if (message.isEmpty()) {
                toast("Cannot send empty message")
                return@setOnClickListener
            }

            val documentSender =
                firestore.collection(String.format(USER_MESSAGES_DOC_REF, database.key, key)).document()
            val documentRec = firestore.collection(String.format(USER_MESSAGES_DOC_REF, key, database.key))
                .document(documentSender.id)
            //todo: check for various supported message types
            val msgData =
                Message(documentSender.id, message, type = if (isText) Message.TYPE_TEXT else Message.TYPE_IMAGE)

            documentSender.set(msgData).addOnCompleteListener { }.addOnFailureListener { }
            documentRec.set(msgData).addOnCompleteListener { }.addOnFailureListener { }
            if (message_view.text.toString().isNotEmpty()) message_view.text?.clear()
            if (adapter.isNotEmpty()) {
                message_grid.smoothScrollToPosition(adapter.bottom)
            }
        }

        add_file_button.setOnClickListener {
            //todo: pick file
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chat_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val intent = intent
        if (intent.hasExtra(EXTRA_USER)) {
            val dataModel = intent.getParcelableExtra<BaseDataModel>(EXTRA_USER)
            if (dataModel is Chat) {
                isFavorite = dataModel.favorite
                isBlocked = dataModel.blocked
            }
        }

        val favMenuItem = menu?.findItem(R.id.menu_favorite)
        favMenuItem?.icon = ContextCompat.getDrawable(
            this,
            if (isFavorite) R.drawable.ic_favorite_linked else R.drawable.ic_favorite_unlinked
        )
        favMenuItem?.title = getString(if (isFavorite) R.string.remove_from_fav else R.string.mark_as_favorite)

        val blockedMenuItem = menu?.findItem(R.id.menu_block)
        blockedMenuItem?.icon = ContextCompat.getDrawable(
            this,
            if (isBlocked) R.drawable.ic_unblock else R.drawable.ic_remove
        )
        blockedMenuItem?.title = getString(if (isBlocked) R.string.unblock_chat else R.string.block_chat)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_block -> {
                if (key.isNotEmpty()) {
                    isBlocked = !isBlocked
                    invalidateOptionsMenu()

                    // Update database
                    firestore.document(String.format(USER_CHATS_DOC_REF, database.key, key))
                        .update(
                            mapOf<String, Any?>(
                                "blocked" to isBlocked
                            )
                        )
                }
            }

            R.id.menu_favorite -> {
                isFavorite = !isFavorite
                invalidateOptionsMenu()

                // Update database
                firestore.document(String.format(USER_CHATS_DOC_REF, database.key, key))
                    .update(
                        mapOf<String, Any?>(
                            "favorite" to isFavorite
                        )
                    )
            }


        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(item: Message) {
        when (item.type) {
            Message.TYPE_IMAGE -> {
            }
            Message.TYPE_VIDEO -> {
            }
            Message.TYPE_AUDIO -> {
            }
            else -> {
            }
        }
    }

    override fun onLongClick(item: Message) {
        toast(item)
    }

    companion object {
        const val EXTRA_USER = "EXTRA_USER"
        const val EXTRA_USER_ID = "EXTRA_USER_ID"
    }
}