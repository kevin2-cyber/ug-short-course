package io.ugshortcourse.voteme.core.services

import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.ugshortcourse.voteme.core.COLLECTION_TOKENS
import io.ugshortcourse.voteme.core.voteMeLogger
import javax.inject.Inject

class VoteMeFirebaseMessaging @Inject constructor() : FirebaseMessagingService() {
    private var firestore: FirebaseFirestore

    private var app: FirebaseApp = FirebaseApp.initializeApp(this)!!

    init {
        firestore = FirebaseFirestore.getInstance(app)
    }

    override fun onNewToken(token: String?) {
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
        token ?: return

        //Store tokens in the registration token database reference
        firestore.collection(COLLECTION_TOKENS).document(app.persistenceKey)
            .set(mapOf("token" to token))
            .addOnCompleteListener {
                voteMeLogger("Token sent")
            }.addOnFailureListener {
                voteMeLogger(it.localizedMessage)
            }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        //Log the data
        voteMeLogger(remoteMessage?.data)
    }
}