package io.codelabs.ugcloudchat.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.codelabs.ugcloudchat.viewmodel.ChatViewModel
import io.codelabs.ugcloudchat.viewmodel.repository.ChatRepository

/**
 * Used to create an instance of the [ChatViewModel]
 */
class ChatViewModelFactory constructor(
    private val application: Application,
    private val repository: ChatRepository
) : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ChatViewModel(application, repository) as T
    }

}