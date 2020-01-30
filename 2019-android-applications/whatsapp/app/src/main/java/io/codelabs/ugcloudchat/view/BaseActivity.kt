package io.codelabs.ugcloudchat.view

import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.codelabs.ugcloudchat.model.WhatsappUser
import io.codelabs.ugcloudchat.model.database.UGCloudChatDao
import io.codelabs.ugcloudchat.model.preferences.UserSharedPreferences
import io.codelabs.ugcloudchat.util.UGCloudChatConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.android.ext.android.inject

abstract class BaseActivity : AppCompatActivity() {
    private val job = Job()

    /**
     * Background Thread
     */
    val ioScope = CoroutineScope(Dispatchers.IO + job)

    /**
     * Main Thread
     */
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    /**
     * Firebase Auth instance
     */
    val auth by inject<FirebaseAuth>()

    /**
     * Firestore instance
     */
    val db by inject<FirebaseFirestore>()

    /**
     * DAO instance
     */
    val dao by inject<UGCloudChatDao>()
    /**
     * User shared preferences instance
     */
    val prefs by inject<UserSharedPreferences>()

    /**
     * Collection of all user documents
     */
    val userCollection = db.collection(UGCloudChatConstants.USERS_COLLECTION)

    /**
     * Document of single user
     */
    fun getUserByUID(uid: String): WhatsappUser? {
        return Tasks.await(userCollection.document(uid).get()).toObject(WhatsappUser::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}