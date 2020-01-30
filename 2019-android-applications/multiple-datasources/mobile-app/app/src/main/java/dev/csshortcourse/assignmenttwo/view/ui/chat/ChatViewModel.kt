package dev.csshortcourse.assignmenttwo.view.ui.chat

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.csshortcourse.assignmenttwo.model.User
import dev.csshortcourse.assignmenttwo.viewmodel.AppViewModel
import kotlinx.coroutines.launch

class ChatViewModel(val app: Application) : AppViewModel(app) {

    private val _users = MutableLiveData<MutableList<User>>()

    init {
        viewModelScope.launch {
            _users.postValue(repo.getUsers(isConnected))
        }
    }

    // Live user's list to observe
    val users: LiveData<MutableList<User>> = _users
}