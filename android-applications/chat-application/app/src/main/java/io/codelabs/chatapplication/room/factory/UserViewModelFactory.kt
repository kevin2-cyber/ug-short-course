package io.codelabs.chatapplication.room.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.codelabs.chatapplication.core.ChatApplication
import io.codelabs.chatapplication.room.viewmodel.UserViewModel

/**
 * User view model factory implementation
 */
class UserViewModelFactory constructor(private val application: ChatApplication) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return UserViewModel(application) as T
    }

}