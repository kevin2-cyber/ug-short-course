package io.codelabs.ugcloudchat.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import io.codelabs.ugcloudchat.model.WhatsappUser
import io.codelabs.ugcloudchat.viewmodel.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class UserViewModel constructor(
    application: Application,
    private val repository: UserRepository
) : AndroidViewModel(application) {

    private val job = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)

    /**
     * Live data for all users
     */
    val users = repository.getAllUsers()

    fun setCurrentUser(user: WhatsappUser) = ioScope.launch {
        repository.setCurrentUser(user)
    }

    fun getCurrentUser(): LiveData<WhatsappUser> = repository.getCurrentUser()

    fun addSingleUser(user: WhatsappUser) = ioScope.launch { repository.addSingleUser(user) }

    fun updateUser(vararg users: WhatsappUser) = ioScope.launch { repository.updateUser(*users) }

    fun getUserByUID(uid: String): LiveData<WhatsappUser> = repository.getUserByUID(uid)

    fun getUserById(id: Long): LiveData<WhatsappUser> = repository.getUserById(id)

    fun addUsers(users: MutableList<WhatsappUser>) = ioScope.launch { repository.addUsers(users) }


    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}