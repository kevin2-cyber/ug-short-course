package io.codelabs.chatapplication.room.viewmodel

import androidx.lifecycle.AndroidViewModel
import io.codelabs.chatapplication.core.ChatApplication
import io.codelabs.chatapplication.data.User
import io.codelabs.chatapplication.room.repository.UserRepository

/**
 * User view model implementation
 */
class UserViewModel constructor(application: ChatApplication) : AndroidViewModel(application) {

    private val repo: UserRepository by lazy { UserRepository(application) }

    fun insert(user: User) = repo.insert(user)

    fun delete(user: User) = repo.delete(user)

    fun update(user: User) = repo.update(user)

    fun deleteAll(vararg user: User) = repo.deleteAll(*user)

    fun getCurrentUser(id: String) = repo.getCurrentUser(id)

    fun getAllUsers() = repo.getAllUsers()

}