package io.codelabs.churchinc.core.datasource.remote

import io.codelabs.churchinc.core.RootActivity
import io.codelabs.churchinc.model.Sermon
import io.codelabs.churchinc.model.User
import io.codelabs.churchinc.util.debugLog
import kotlinx.coroutines.launch


fun RootActivity.getLiveUser(callback: DataCallback<User>) {
    firestore.collection("users").document(auth.uid!!)
        .addSnapshotListener(this) { snapshot, exception ->
            if (exception != null) {
                callback.onError(exception.localizedMessage)
                return@addSnapshotListener
            }

            // Get user's data from the database
            val user = snapshot?.toObject(User::class.java)
            if (user == null) {
                callback.onError("User could not be found")
            } else {
                ioScope.launch {
                    val currentUser: User? = dao.getCurrentUser(auth.uid!!)
                    if (currentUser == null) dao.createUser(user) else dao.updateUser(user)

                    uiScope.launch {
                        callback.onComplete(user)
                    }
                }
            }

        }
}

fun RootActivity.getSermons(callback: DataCallback<MutableList<Sermon>>) {
    firestore.collection("sermons").addSnapshotListener(this) { snapshot, exception ->
        if (exception != null) {
            callback.onError(exception.localizedMessage)
            return@addSnapshotListener
        }

        debugLog("Sermons are: ${snapshot?.documents}")
        val sermons = snapshot?.toObjects(Sermon::class.java)
        if (sermons == null) callback.onError("There are no sermons in the database")
        else callback.onComplete(sermons)
    }
}

interface DataCallback<T> {

    fun onError(e: String?)

    fun onComplete(result: T?)

}