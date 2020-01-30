package io.codelabs.ugcloudchat.viewmodel.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import io.codelabs.ugcloudchat.model.Chat
import io.codelabs.ugcloudchat.model.database.UGCloudChatDao
import io.codelabs.ugcloudchat.model.preferences.UserSharedPreferences
import io.codelabs.ugcloudchat.util.UGCloudChatConstants
import io.codelabs.ugcloudchat.util.debugThis
import io.codelabs.ugcloudchat.util.getUniqueChatPathWith
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChatRepository private constructor(
    val dao: UGCloudChatDao,
    val prefs: UserSharedPreferences,
    val db: FirebaseFirestore
) {
//    private var chats: LiveData<PagedList<Chat>>? = null

    fun getMyChatsWith(uid: String): LiveData<MutableList<Chat>> =
        dao.getChatsBetween(prefs.uid, uid)

    fun getOnlineMessagesWith(recipient: String): LiveData<MutableList<Chat>> {
        val liveData = MutableLiveData<MutableList<Chat>>()
        val path = prefs.uid?.getUniqueChatPathWith(recipient)

        try {
            db.collection(
                String.format(
                    UGCloudChatConstants.CHATS_COLLECTION,
                    path
                )
            ).orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        // Send an empty list as the callback result
                        liveData.postValue(mutableListOf())
                        return@addSnapshotListener
                    }

                    // Convert all data from the snapshot to chats
                    val chats = snapshot?.toObjects(Chat::class.java)

                    if (chats == null) {
                        liveData.postValue(mutableListOf())
                        debugThis("No messages found in the snapshot")
                    } else {
                        liveData.postValue(chats)
                    }

                }
        } catch (e: Exception) {
            debugThis("Unable to load message: ${e.localizedMessage}")
        }
        return liveData
    }

    suspend fun sendMessage(chat: Chat) = withContext(Dispatchers.IO) {
        try {
            Tasks.await(
                db.collection(
                    String.format(
                        UGCloudChatConstants.CHATS_COLLECTION,
                        prefs.uid?.getUniqueChatPathWith(chat.recipient)
                    )
                ).document().apply {
                    chat.key = id
                }.set(chat)
            )
            null
        } catch (e: Exception) {
            debugThis("Unable to send message: ${e.localizedMessage}")
        }
    }

    suspend fun deleteChat(chat: String) = withContext(Dispatchers.IO) {
        /*todo: delete chat*/
    }

    companion object {
        @Volatile
        private var instance: ChatRepository? = null

        fun getInstance(
            dao: UGCloudChatDao,
            prefs: UserSharedPreferences,
            db: FirebaseFirestore
        ): ChatRepository =
            instance ?: synchronized(this) {
                instance ?: ChatRepository(dao, prefs, db).also { instance = it }
            }
    }
}