package io.codelabs.chatapplication.core

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import io.codelabs.chatapplication.core.datasource.UserDatabase
import io.codelabs.chatapplication.module.appModule
import io.codelabs.chatapplication.module.firebaseModule
import io.codelabs.chatapplication.module.roomModule
import io.codelabs.chatapplication.util.USER_DOC_REF
import io.codelabs.chatapplication.util.debugLog
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ChatApplication : Application() {

    private val database: UserDatabase by lazy { UserDatabase.getInstance(this) }

    override fun onCreate() {
        super.onCreate()

        // Init Firebase
        FirebaseApp.initializeApp(this)


        // Init Koin
        startKoin {
            androidContext(this@ChatApplication)

            modules(appModule, roomModule, firebaseModule)

        }

        postStatus()

    }

    private fun postStatus() {
        if (database.isLoggedIn && !database.key.isNullOrEmpty()) {
            try {
                FirebaseFirestore.getInstance().document(String.format(USER_DOC_REF, database.key))
                    .update(
                        mapOf<String, Any?>(
                            "online" to true
                        )
                    ).addOnCompleteListener {
                        debugLog("Updated user status")
                    }
            } catch (e: Exception) {
                debugLog(e.cause)
                debugLog(e.localizedMessage)
            }
        }
    }

}