package io.codelabs.ugcloudchat.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.codelabs.ugcloudchat.model.Chat
import io.codelabs.ugcloudchat.viewmodel.repository.ChatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ChatViewModel constructor(
    application: Application,
    private val repository: ChatRepository
) : AndroidViewModel(application) {
    private val job = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)
//    private var chatDataSourceFactory: ChatDataSourceFactory? = null

//    private var chats: LiveData<PagedList<Chat>>? = null

    /*fun getChatsWith(recipient: String): LiveData<PagedList<Chat>> {
        chatDataSourceFactory =
            ChatDataSourceFactory(
                repository.db,
                repository.dao,
                recipient,
                repository.prefs,
                ioScope
            )
        val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(20)
            .build()
        chats = LivePagedListBuilder<String, Chat>(chatDataSourceFactory!!, config)
            .setBoundaryCallback(ChatBoundaryCallback())
            //.setInitialLoadKey(null)
            .build()
        return chats!!
    }
*/

/*
    fun getState(): LiveData<DatabaseState> =
        Transformations.switchMap<ChatDataSource, DatabaseState>(
            chatDataSourceFactory!!.liveDataSource, ChatDataSource::state
        )
*/

//    val isEmptyChat: Boolean get() = chats?.value?.isEmpty() ?: true

    fun sendMessage(chat: Chat) = ioScope.launch { repository.sendMessage(chat) }

    fun deleteChat(chat: String) = ioScope.launch { repository.deleteChat(chat) }

    fun getMyChatsWith(recipient: String): LiveData<MutableList<Chat>> {
        val liveData = MutableLiveData<MutableList<Chat>>()
        ioScope.launch {
            val conversation = repository.getMyChatsWith(recipient).value
            liveData.postValue(conversation)
        }
        return liveData
    }

    // Replace with this method
    fun getOnlineMessagesWith(recipient: String): LiveData<MutableList<Chat>>  = repository.getOnlineMessagesWith(recipient)

//    fun retry() = chatDataSourceFactory?.liveDataSource?.value?.retry()

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}