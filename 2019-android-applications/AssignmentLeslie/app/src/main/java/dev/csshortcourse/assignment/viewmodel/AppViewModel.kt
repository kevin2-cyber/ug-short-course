package dev.csshortcourse.assignment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.csshortcourse.assignment.model.User
import dev.csshortcourse.assignment.viewmodel.repo.Repository

class AppViewModel : ViewModel() {

    private val repo: Repository by lazy { Repository(FirebaseDataSource()) }

    fun createAccount(
        username: String,
        email: String,
        password: String,
        callback: Callback<Response, User?>
    ) = repo.createAccount(username, email, password, callback)

    fun login(
        email: String,
        password: String,
        callback: Callback<Response, User?>
    ) = repo.login(email, password, callback)

    fun getCurrentUser(): LiveData<User> = repo.getCurrentUser()

}