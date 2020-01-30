package io.codelabs.ugcloudchat.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.codelabs.ugcloudchat.viewmodel.UserViewModel
import io.codelabs.ugcloudchat.viewmodel.repository.UserRepository

/**
 * Used to create an instance of the [UserViewModel]
 */
class UserViewModelFactory constructor(
    private val application: Application,
    private val repository: UserRepository
) : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserViewModel(application, repository) as T
    }

}