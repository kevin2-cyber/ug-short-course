package io.shortcourse.firebasetestapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize firebase application
        FirebaseApp.initializeApp(this@MainActivity).also {
            debugLog(it?.name)
        }

        // Initialize the Firebase Auth Object
        auth = FirebaseAuth.getInstance()
    }

    fun signIn(v: View?) {
        if (validateForm(email, password)) {

            if (validateEmail(email)) {
                debugLog("Email and password are valid")

                auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                        .addOnCompleteListener(this@MainActivity) { task ->
                            if (task.isSuccessful) {
                                toast("Welcome to this application")
                                startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                                finishAfterTransition()
                            } else {
                                debugLog("Cause: ${task.exception?.cause}")
                                toast(task.exception?.localizedMessage)
                            }
                        }.addOnFailureListener(this@MainActivity) { ex ->
                            debugLog("Cause: ${ex.cause}")
                            toast(ex.localizedMessage)
                        }

            } else toast("Invalid email address")
        } else {
            toast("Invalid inputs")
        }
    }

    fun createAccount(v: View?) {
        if (validateForm(email, password)) {

            if (validateEmail(email)) {
                debugLog("Email and password are valid")

                auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                        .addOnCompleteListener(this@MainActivity) { task ->
                            if (task.isSuccessful) {
                                toast("Welcome to this application")
                                startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                                finishAfterTransition()
                            } else {
                                debugLog("Cause: ${task.exception?.cause}")
                                toast(task.exception?.localizedMessage)
                            }
                        }.addOnFailureListener(this@MainActivity) { ex ->
                            debugLog("Cause: ${ex.cause}")
                            toast(ex.localizedMessage)
                        }

            } else toast("Invalid email address")
        } else {
            toast("Invalid inputs")
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user == null) {
            // No logged in user
        } else {
            // User is logged in proceed to Home page
            toast("Logged in as: ${user.email}")
            startActivity(Intent(this@MainActivity, HomeActivity::class.java))
            finishAfterTransition()
        }
    }
}
