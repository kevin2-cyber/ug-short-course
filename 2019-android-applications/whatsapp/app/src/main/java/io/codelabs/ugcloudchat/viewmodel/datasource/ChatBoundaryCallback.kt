package io.codelabs.ugcloudchat.viewmodel.datasource

import androidx.paging.PagedList
import io.codelabs.ugcloudchat.model.Chat
import io.codelabs.ugcloudchat.util.debugThis

class ChatBoundaryCallback : PagedList.BoundaryCallback<Chat>() {

    override fun onZeroItemsLoaded() {
        debugThis("onZeroItemsLoaded called")
    }

    override fun onItemAtEndLoaded(itemAtEnd: Chat) {
        debugThis("onItemAtEndLoaded called")
    }

    override fun onItemAtFrontLoaded(itemAtFront: Chat) {
        debugThis("onItemAtFrontLoaded called")
    }
}