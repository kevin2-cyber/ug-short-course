package io.codelabs.churchinc.view.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import io.codelabs.churchinc.R
import io.codelabs.churchinc.core.RootActivity
import io.codelabs.churchinc.model.User
import io.codelabs.churchinc.util.InputValidator
import io.codelabs.churchinc.util.toast
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.launch

class RegisterActivity : RootActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    fun doRegister(v: View) {
        if (!InputValidator.validateField(user_full_name)) {
            toast("Invalid Username")
            return
        }

        if (!InputValidator.validateEmails(user_email)) {
            toast("Invalid email address")
            return
        }

        if (!InputValidator.validateField(user_password)) {
            toast("Invalid Password")
            return
        }

        toast("Creating your new account....")
        InputValidator.toggleFields(false, user_email, user_password, register_button)
        auth.createUserWithEmailAndPassword(user_email.text.toString(), user_password.text.toString())
            .addOnCompleteListener(this@RegisterActivity) {
                if (it.isSuccessful) {
                    // Get the firebase User
                    val firebaseUser = it.result?.user

                    // Create a new User instance from the firebase user above
                    val currentUser = User(firebaseUser!!.uid, user_full_name.text.toString(), firebaseUser.photoUrl.toString())

                    // Store user data locally
                    ioScope.launch {
                        dao.createUser(currentUser)

                        uiScope.launch {

                            firestore.collection("users").document(auth.uid!!)
                                .set(currentUser)
                                .addOnFailureListener(this@RegisterActivity) { ex ->
                                    InputValidator.toggleFields(true, user_email, user_password, register_button)
                                    toast(ex.localizedMessage)
                                }.addOnCompleteListener(this@RegisterActivity) { task ->
                                    if (task.isSuccessful) {
                                        toast("Logged in as ${currentUser.name}")
                                        val homeIntent = Intent(this@RegisterActivity, HomeActivity::class.java)
                                        startActivity(homeIntent)
                                        finishAfterTransition()
                                    } else {
                                        InputValidator.toggleFields(true, user_email, user_password, register_button)
                                        toast(it.exception?.localizedMessage!!)
                                    }
                                }
                        }
                    }
                } else {
                    InputValidator.toggleFields(true, user_email, user_password, register_button)
                    toast(it.exception?.localizedMessage!!)
                }
            }.addOnFailureListener(this) {
                InputValidator.toggleFields(true, user_email, user_password, register_button)
                toast(it.localizedMessage)
            }
    }
}
