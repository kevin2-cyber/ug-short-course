package io.shortcourse.firebasetestapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()
        db = Firebase.firestore
        user_details.text = String.format("Current user: %s", auth.currentUser?.email)

        db.collection("users").document(auth.uid!!).addSnapshotListener(this@HomeActivity) { snapshot, exception ->
            if (exception != null) {
                debugLog(exception.localizedMessage)
                return@addSnapshotListener
            }

            debugLog("Data from database: ${snapshot?.data}")
        }

    }

    fun logout(v: View?) {
        auth.signOut()
        toast("Signed out successfully")
        startActivity(Intent(this@HomeActivity, MainActivity::class.java))
        finish()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        val user = HashMap<String, Any?>()
        user["username"] = currentUser?.displayName
        user["email"] = currentUser?.email
        user["lastSeen"] = System.currentTimeMillis()

        db.collection("users").document(currentUser?.uid!!)
                .set(user, SetOptions.merge())
                .addOnSuccessListener { debugLog("DocumentSnapshot successfully written!") }
                .addOnFailureListener { debugLog("Error writing document.\n${it.localizedMessage}") }
    }
}
