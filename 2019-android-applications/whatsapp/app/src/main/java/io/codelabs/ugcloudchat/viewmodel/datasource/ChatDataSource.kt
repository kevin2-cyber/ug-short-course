package io.codelabs.ugcloudchat.viewmodel.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemKeyedDataSource
import com.google.firebase.firestore.FirebaseFirestore
import io.codelabs.ugcloudchat.model.Chat
import io.codelabs.ugcloudchat.model.database.UGCloudChatDao
import io.codelabs.ugcloudchat.model.preferences.UserSharedPreferences
import io.codelabs.ugcloudchat.util.UGCloudChatConstants
import io.codelabs.ugcloudchat.util.debugThis
import io.codelabs.ugcloudchat.util.getUniqueChatPathWith
import io.codelabs.ugcloudchat.viewmodel.repository.DatabaseState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ChatDataSource(
    private val db: FirebaseFirestore,
    private val dao: UGCloudChatDao,
    private val recipient: String,
    private val prefs: UserSharedPreferences,
    private val executor: CoroutineScope
) : ItemKeyedDataSource<String, Chat>() {
    private var retry: (() -> Any)? = null

    /**
     * Stores the state of the database loader
     */
    var state = MutableLiveData<DatabaseState>()

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<Chat>
    ) {
        state.postValue(DatabaseState.LOADING)
        if (params.requestedInitialKey.isNullOrEmpty()) {
            state.postValue(DatabaseState.ERROR)
            callback.onResult(mutableListOf())
        } else {
            try {
                executor.launch {
                    db.collection(
                        String.format(
                            UGCloudChatConstants.CHATS_COLLECTION,
                            prefs.uid?.getUniqueChatPathWith(recipient)
                        )
                    ).addSnapshotListener { snapshot, exception ->
                        if (exception != null) {
                            debugThis("Error loading chats: ${exception.localizedMessage}")
                            state.postValue(DatabaseState.ERROR)
                            callback.onResult(mutableListOf())
                            return@addSnapshotListener
                        }

                        val chats = snapshot?.toObjects(Chat::class.java)
                        if (chats == null) {
                            callback.onResult(mutableListOf())
                            retry = { loadInitial(params, callback) }
                            state.postValue(DatabaseState.UNKNOWN)
                        } else {
                            callback.onResult(chats)
                            retry = null
                            state.postValue(DatabaseState.LOADED)
                            // Add all chats to the local database
                            dao.addChats(chats)
                        }

                    }
                }
            } catch (e: Exception) {
                debugThis("Unable to load chats: ${e.localizedMessage}")
                retry = {
                    loadInitial(params, callback)
                }
                state.postValue(DatabaseState.ERROR)
            }
        }
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<Chat>) {
        state.postValue(DatabaseState.LOADING)

        if (params.key.isEmpty()) {
            state.postValue(DatabaseState.ERROR)
            callback.onResult(mutableListOf())
        } else {
            try {
                executor.launch {
                    db.collection(
                        String.format(
                            UGCloudChatConstants.CHATS_COLLECTION,
                            prefs.uid?.getUniqueChatPathWith(recipient)
                        )
                    ).addSnapshotListener { snapshot, exception ->
                        if (exception != null) {
                            debugThis("Error loading chats: ${exception.localizedMessage}")
                            state.postValue(DatabaseState.ERROR)
                            callback.onResult(mutableListOf())
                            return@addSnapshotListener
                        }

                        val chats = snapshot?.toObjects(Chat::class.java)
                        if (chats == null) {
                            callback.onResult(mutableListOf())
                            retry = { loadAfter(params, callback) }
                            state.postValue(DatabaseState.UNKNOWN)
                        } else {
                            callback.onResult(chats)
                            retry = null
                            state.postValue(DatabaseState.LOADED)
                            // Add all chats to the local database
                            dao.addChats(chats)
                        }

                    }
                }
            } catch (e: Exception) {
                debugThis("Unable to load chats: ${e.localizedMessage}")
                retry = {
                    loadAfter(params, callback)
                }
                state.postValue(DatabaseState.ERROR)
            }
        }
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<Chat>) {
        /* Do Nothing here */
    }

    override fun getKey(item: Chat): String = item.key

    /**
     * Retry transaction
     */
    fun retry() = retry
}