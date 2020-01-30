package dev.csshortcourse.assignment.viewmodel.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.csshortcourse.assignment.model.User
import dev.csshortcourse.assignment.viewmodel.Callback
import dev.csshortcourse.assignment.viewmodel.FirebaseDataSource
import dev.csshortcourse.assignment.viewmodel.Response

/**
 * Base repository to show which calls are allowed to be made to the backend
 */
interface BaseRepository {
    fun createAccount(
        username: String,
        email: String,
        password: String,
        callback: Callback<Response, User?>
    )

    fun login(email: String, password: String, callback: Callback<Response, User?>)
    fun getCurrentUser(): LiveData<User>
}

/**
 * Repository for making calls to the respective data sources of the application
 */
class Repository(private val datasource: FirebaseDataSource) : BaseRepository {

    override fun createAccount(
        username: String,
        email: String,
        password: String,
        callback: Callback<Response, User?>
    ) {
        datasource.createAccount(username, email, password, callback)
    }

    override fun login(email: String, password: String, callback: Callback<Response, User?>) {
        datasource.login(email, password, callback)
    }

    override fun getCurrentUser(): LiveData<User> {
        val liveUser = MutableLiveData<User>()
        datasource.getUserInformation { response, user ->
            if (user != null) {
                liveUser.postValue(user)
            }
        }
        return liveUser
    }

}