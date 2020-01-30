package io.codelabs.churchinc.view.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import io.codelabs.churchinc.R
import io.codelabs.churchinc.core.RootActivity
import io.codelabs.churchinc.model.User
import io.codelabs.churchinc.util.InputValidator
import io.codelabs.churchinc.util.toast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.launch

class LoginActivity : RootActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

    }

    fun doLogin(v: View?) {
        if (!InputValidator.validateEmails(user_email)) {
            toast("Invalid email address")
            return
        }

        if (!InputValidator.validateField(user_password)) {
            toast("Invalid Password")
            return
        }

        toast("Signing in to your account....")
        InputValidator.toggleFields(false, user_email, user_password, login_button, create_account_button)
        auth.signInWithEmailAndPassword(user_email.text.toString(), user_password.text.toString())
            .addOnCompleteListener(this@LoginActivity) {
                if (it.isSuccessful) {
                    // Get the firebase User
                    val firebaseUser = it.result?.user

                    // Create a new User instance from the firebase user above
                    val currentUser = User(firebaseUser!!.uid, firebaseUser.email!!, firebaseUser.photoUrl.toString())

                    // Store user data locally
                    ioScope.launch {
                        dao.createUser(currentUser)

                        uiScope.launch {
                            toast("Logged in as ${currentUser.name}")
                            val homeIntent = Intent(this@LoginActivity, HomeActivity::class.java)
                            startActivity(homeIntent)
                            finishAfterTransition()
                        }
                    }
                } else {
                    InputValidator.toggleFields(true, user_email, user_password, login_button, create_account_button)
                    toast(it.exception?.localizedMessage!!)
                }
            }.addOnFailureListener(this) {
                InputValidator.toggleFields(true, user_email, user_password, login_button, create_account_button)
                toast(it.localizedMessage)
            }
    }

    fun navToRegister(v: View?) = startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
}
