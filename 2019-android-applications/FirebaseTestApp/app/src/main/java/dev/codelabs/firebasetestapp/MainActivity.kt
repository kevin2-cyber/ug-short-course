package dev.codelabs.firebasetestapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.Slide
import androidx.transition.TransitionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val rtDatabase: FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Login to an existing account
        login_button.setOnClickListener {
            val emailField = email.text.toString()
            val pwdField = password.text.toString()

            // Create account with firebase
            auth.signInWithEmailAndPassword(emailField, pwdField)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        Toast.makeText(
                            this@MainActivity, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        updateUI(null)
                    }
                }.addOnFailureListener(this) { ex ->
                    debugger("An error occurred while creating a new user. ${ex.localizedMessage}")
                }
        }

        // Create a new account for the user
        create_account_button.setOnClickListener {
            val emailField = email.text.toString()
            val pwdField = password.text.toString()

            // Create account with firebase
            auth.createUserWithEmailAndPassword(emailField, pwdField)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        Toast.makeText(
                            this@MainActivity, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        updateUI(null)
                    }
                }.addOnFailureListener(this) { ex ->
                    debugger("An error occurred while creating a new user. ${ex.localizedMessage}")
                }
        }

        get_started_button.apply {
            setOnClickListener {
                startActivity(Intent(this@MainActivity, WelcomeActivity/*OtherActivity*/::class.java))
                finishAfterTransition()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(user: FirebaseUser?) {
        debugger("User is: $user")

        // Toggle button state
        Handler().postDelayed({
            TransitionManager.beginDelayedTransition(container)
            with(user != null) {
                get_started_button.isEnabled = this
                login_button.isEnabled = !this
                create_account_button.isEnabled = !this

                if (user != null) {
                    // Store info in database
                    storeInDatabase(user, true)
                    storeInDatabase(user, false)
                }
            }
        }, 1200)
    }

    private fun storeInDatabase(user: FirebaseUser, isFirestore: Boolean) {
        if (isFirestore) {
            // Store user information in Firebase Firestore
            debugger("Storing in firestore")

            firestore.collection(USER_REF).document(user.uid).set(AppUser(user.uid, user.email))
                .addOnCompleteListener(this) {
                    debugger("user stored")
                }
                .addOnFailureListener(this) {
                    debugger("Unable to store user information")
                }

            // Check whether user data exists
            firestore.runTransaction { transaction ->
                val userDoc = transaction.get(firestore.collection(USER_REF).document(user.uid))

                if (userDoc.exists()) {
                    val appUser: AppUser? = userDoc.toObject(AppUser::class.java)
                    debugger("Existing user => $appUser")
                    // todo: store user in local database for future reference
                } else {
                    val appUser = AppUser(user.uid, user.email)
                    transaction.set(
                        firestore.collection(USER_REF).document(user.uid),
                        appUser, SetOptions.merge()
                    )
                    debugger("New user => $appUser")
                    // todo: store user in local database for future reference
                }
                null
            }
        } else {
            // Store user information in Firebase Real-Time Database
            debugger("Storing in real-time database")

            val userRef = rtDatabase.reference.child(USER_REF).child(user.uid)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    debugger(p0.message)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        val appUser = p0.getValue(AppUser::class.java)
                        debugger("RTDB Existing user => $appUser")
                        // todo: store user in local database for future reference
                    } else {
                        // Create new user object
                        val appUser = AppUser(user.uid, user.email)

                        // Store in database
                        userRef.setValue(appUser)
                            .addOnCompleteListener(this@MainActivity) {
                                if (it.isSuccessful) {
                                    debugger("New user => $appUser")
                                    // todo: store user in local database for future reference
                                } else {
                                    debugger(it.exception?.localizedMessage)
                                }
                            }
                            .addOnFailureListener(this@MainActivity) {
                                debugger(it.localizedMessage)
                            }
                    }
                }
            })
        }
    }
}

