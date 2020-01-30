package dev.csshortcourse.assignment.viewmodel

import android.util.Patterns
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import dev.csshortcourse.assignment.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

/**
 *   ARCHITECTURE OF THE APPLICATION
 *   *******************************
 *   Send request
 *   VIEW => VIEW-MODEL => REPOSITORY => DATA-SOURCE
 *
 *   Receive response
 *   DATA-SOURCE => REPOSITORY => VIEW-MODEL => VIEW
 *
 *   This class is responsible for handling actions to perform the following functions:
 *   1. Login user
 *   2. Create user account
 *   3. Fetch user information from database in real-time
 */
class FirebaseDataSource {
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val firestore by lazy { FirebaseFirestore.getInstance() }

    // Coroutines
    private val ioScope = CoroutineScope(IO)
    private val uiScope = CoroutineScope(Main)

    fun login(email: String, password: String, callback: Callback<Response, User?>) {
        callback(Response.LOADING, null)
        // Validate email and password
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            callback(Response.ERROR, null)
            return
        }

        ioScope.launch {
            try {

                // Sign in user
                val firebaseUser =
                    Tasks.await(auth.signInWithEmailAndPassword(email, password)).user

                if (firebaseUser == null) {
                    callback(Response.ERROR, null)
                    return@launch
                }

                // Get user information
                val snapshot =
                    Tasks.await(firestore.collection(Utils.USER_REF).document(firebaseUser.uid).get())

                if (!snapshot.exists()) {
                    callback(Response.ERROR, null)
                    return@launch
                }

                uiScope.launch {
                    // Get user's information from the database
                    val user = snapshot.toObject(User::class.java)
                    callback(Response.COMPLETED, user)
                }
            } catch (e: Exception) {
                // elvis
                callback(Response.ERROR, null)
            }
        }

    }

    fun createAccount(
        username: String,
        email: String,
        password: String,
        callback: Callback<Response, User?>
    ) {
        callback(Response.LOADING, null)
        // Validate email and password
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            callback(Response.ERROR, null)
            return
        }

        ioScope.launch {
            try {

                // Create user account
                val firebaseUser =
                    Tasks.await(auth.createUserWithEmailAndPassword(email, password)).user

                if (firebaseUser == null) {
                    callback(Response.ERROR, null)
                    return@launch
                }

                // Store user information
                val user = User(firebaseUser.uid, username, firebaseUser.email ?: email)
                Tasks.await(
                    firestore.collection(Utils.USER_REF).document(firebaseUser.uid).set(
                        user,
                        SetOptions.merge()
                    )
                )

                uiScope.launch {
                    callback(Response.COMPLETED, user)
                }
            } catch (e: Exception) {
                // elvis
                callback(Response.ERROR, null)
            }
        }
    }

    fun getUserInformation(callback: Callback<Response, User?>) {
        // Use firebase authentication to tell if a user is signed in or not
        callback(Response.LOADING, null)
        if (auth.currentUser != null) {
            ioScope.launch {
                val uid = auth.currentUser?.uid
                if (uid.isNullOrEmpty()) {
                    callback(Response.ERROR, null)
                    return@launch
                }

                try {// Get user information
                    val snapshot =
                        Tasks.await(firestore.collection(Utils.USER_REF).document(uid).get())

                    if (!snapshot.exists()) {
                        callback(Response.ERROR, null)
                        return@launch
                    }

                    uiScope.launch {
                        // Get user's information from the database
                        val user = snapshot.toObject(User::class.java)
                        callback(Response.COMPLETED, user)
                    }
                } catch (e: Exception) {
                    callback(Response.ERROR, null)
                }
            }


        } else {
            callback(Response.ERROR, null)
        }
    }
}