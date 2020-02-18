package shortcourse.readium.core

import android.app.Application
import android.os.StrictMode
import com.bugsnag.android.Bugsnag
import com.google.firebase.FirebaseApp
import shortcourse.blogly.util.debugger
import sqip.InAppPaymentsSdk

/**
 * [Readium] is the main entry point of the application
 */
class Readium : Application() {

    override fun onCreate() {
        enableStrictMode()
        super.onCreate()
        
        // Enable Bugsnag
        Bugsnag.init(this@Readium)

        // Enable Firebase
        FirebaseApp.initializeApp(this@Readium).apply {
            debugger("Application started successfully")
        }


        // In-App payment
        InAppPaymentsSdk.squareApplicationId = "sandbox-sq0idb-pgfYgj6x48Ees6YXgf5g9w"
        // EAAAEBAzLlBTdeAVax2ZhVPbaoa-9fXruM31ipNc7NMgKubbR8OlMmMZZlMdz7it
    }


    // Thread policy implementation
    private fun enableStrictMode() = StrictMode.ThreadPolicy.Builder()
        .detectAll()
        .penaltyLog()
        .build()

}