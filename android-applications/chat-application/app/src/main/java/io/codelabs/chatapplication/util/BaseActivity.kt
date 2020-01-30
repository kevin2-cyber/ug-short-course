package io.codelabs.chatapplication.util

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage
import io.codelabs.chatapplication.BuildConfig
import io.codelabs.chatapplication.core.ChatApplication
import io.codelabs.chatapplication.core.datasource.UserDatabase
import io.codelabs.chatapplication.room.factory.UserViewModelFactory
import io.codelabs.chatapplication.room.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

/**
 * Base class for all activities
 */
abstract class BaseActivity : AppCompatActivity() {
    private val job = Job()
    protected val ioScope = CoroutineScope(Dispatchers.IO + job)
    protected val uiScope = CoroutineScope(Dispatchers.Main + job)

    val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    val firestore: FirebaseFirestore by lazy {
        val instance = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(BuildConfig.DEBUG).build()
        instance.firestoreSettings = settings
        instance

    }
    val bucket: FirebaseStorage by lazy { FirebaseStorage.getInstance() }
    val database: UserDatabase by lazy { UserDatabase.getInstance(application as ChatApplication) }

    protected lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        debugLog("ActivityID: ${this::class.java.name}")

        userViewModel = ViewModelProviders.of(this, UserViewModelFactory(application as ChatApplication))
            .get(UserViewModel::class.java)

        onViewCreated(savedInstanceState, intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    abstract val layoutId: Int

    abstract fun onViewCreated(instanceState: Bundle?, intent: Intent)

}