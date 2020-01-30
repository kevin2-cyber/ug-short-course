package io.codelabs.chatapplication.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.GoogleAuthProvider
import io.codelabs.chatapplication.R
import io.codelabs.chatapplication.data.User
import io.codelabs.chatapplication.util.*
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.coroutines.launch

/**
 * Activity to handle [User] authentication
 */
class AuthActivity(override val layoutId: Int = R.layout.activity_auth) : BaseActivity() {

    override fun onViewCreated(instanceState: Bundle?, intent: Intent) {
        username.addTextChangedListener(textWatcher)
        password.addTextChangedListener(textWatcher)

        login_button.setOnClickListener { loginUser() }
        google_login_button.setOnClickListener { googleLoginInit() }
    }

    private fun googleLoginInit() {
        toggleFields()

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(/*getString(R.string.default_web_client_id)*/WEB_CLIENT_ID)
            .requestEmail()
            .build()

        // Create SignIn Client
        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Start SignIn
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun loginUser() {
        val usr = username.text.toString()
        val pwd = password.text.toString()

        toggleFields(false)
        auth.signInWithEmailAndPassword(usr, pwd)
            .addOnCompleteListener(this@AuthActivity) {
                if (it.isSuccessful) {
                    firestore.document(String.format(USER_DOC_REF, auth.uid))
                        .get()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                ioScope.launch {
                                    val user = task.result?.toObject(User::class.java)
                                    if (user != null) userViewModel.insert(user)

                                    uiScope.launch {
                                        toggleFields()
                                        toast(message = "Logged in successfully")
                                        intentTo(HomeActivity::class.java, true)
                                    }

                                }
                            }
                        }.addOnFailureListener {
                            toggleFields()
                            toast(message = "Could not retrieve logged in user's information")
                        }
                }
            }.addOnFailureListener(this@AuthActivity) { exception ->
                debugLog(exception.cause)
                toast("Login failed. Please try again later")
                toggleFields()
            }

    }

    private fun toggleFields(b: Boolean = true) {
        username.isEnabled = b
        password.isEnabled = b
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            login_button.isEnabled = setButtonState()
        }
    }

    private fun setButtonState(): Boolean {
        val usr = username.text.toString()
        val pwd = password.text.toString()

        // Username is not empty
        // Password is not empty
        // Username matches email address format
        // Password length is not less than 6
        return usr.isNotEmpty() && pwd.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(usr).matches() && pwd.length > 5
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    try {
                        // Google Sign In was successful, authenticate with Firebase
                        val account = task.getResult(ApiException::class.java)
                        if (account != null) firebaseAuthWithGoogle(account) else toast(message = "Unable to get your Google account details")
                    } catch (e: ApiException) {
                        // Google Sign In failed, update UI appropriately
                        debugLog(e.localizedMessage)
                        toast(message = "We were unable to sign in to your account at this time.lease try again later")
                    }
                }

                else -> toast(message = "Login was cancelled")
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        toast(message = "Setting up your account...")
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    debugLog("signInWithCredential:success")
                    val currentUser = auth.currentUser!!

                    val user = User(
                        currentUser.uid,
                        currentUser.displayName ?: currentUser.email ?: "No username",
                        currentUser.photoUrl.toString()
                    )

                    // Add user information to the database
                    val document = firestore.document(String.format(USER_DOC_REF, currentUser.uid))
                    document.get().addOnCompleteListener {

                        if (it.isSuccessful) {
                            val newUser = it.result?.toObject(User::class.java)
                            if (newUser != null) {
                                ioScope.launch {
                                    userViewModel.insert(newUser)
                                    database.key = currentUser.uid

                                    uiScope.launch {
                                        toggleFields()
                                        toast(message = "Logged in successfully")
                                        intentTo(HomeActivity::class.java, true)
                                    }
                                }
                            } else {

                                // Create new document
                                document.set(user)
                                    .addOnCompleteListener {
                                        ioScope.launch {
                                            userViewModel.insert(user)
                                            database.key = currentUser.uid

                                            uiScope.launch {
                                                toggleFields()
                                                toast(message = "Logged in successfully")
                                                intentTo(HomeActivity::class.java, true)
                                            }
                                        }
                                    }
                            }
                        } else {
                            toggleFields()
                            toast(it.exception?.localizedMessage)
                        }
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    debugLog("signInWithCredential:failure")
                    toggleFields()
                    Snackbar.make(container, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                }

            }.addOnFailureListener {
                // If sign in fails, display a message to the user.
                debugLog(it.localizedMessage)
                toggleFields()
                Snackbar.make(container, "Authentication Failed. ${it.localizedMessage}", Snackbar.LENGTH_SHORT).show()
            }
    }

    companion object {
        private const val RC_SIGN_IN = 9
    }
}