package io.codelabs.chatapplication.core.firebase

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.codelabs.chatapplication.util.debugLog

class ChatFirebaseMessaging : FirebaseMessagingService() {

    override fun onMessageReceived(rm: RemoteMessage?) {
        if (rm != null) {
            val data = rm.data
            debugLog(data)


        }
    }

}