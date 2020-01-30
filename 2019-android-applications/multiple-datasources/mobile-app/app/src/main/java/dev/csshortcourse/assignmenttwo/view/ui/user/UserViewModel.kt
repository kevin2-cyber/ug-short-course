package dev.csshortcourse.assignmenttwo.view.ui.user

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.csshortcourse.assignmenttwo.model.User
import dev.csshortcourse.assignmenttwo.viewmodel.AppViewModel
import dev.csshortcourse.assignmenttwo.viewmodel.repository.Callback
import kotlinx.coroutines.launch

class UserViewModel(app: Application) : AppViewModel(app) {
    private val _users = MutableLiveData<MutableList<User>>()

    init {
        viewModelScope.launch {
            _users.postValue(repo.getUsers(isConnected))
        }
    }

    fun login(user: User, callback: Callback<User>) {
        viewModelScope.launch {
            repo.login(user, callback)
        }
    }

    fun logout(callback: Callback<Void>) {
        viewModelScope.launch {
            repo.logout(callback)
        }
    }

    val users: LiveData<MutableList<User>> get() = _users
}
