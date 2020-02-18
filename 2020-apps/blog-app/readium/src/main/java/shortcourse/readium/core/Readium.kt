package shortcourse.readium.core

import android.app.Application
import android.os.StrictMode
import com.bugsnag.android.Bugsnag
import com.google.firebase.FirebaseApp
import shortcourse.readium.BuildConfig
import shortcourse.readium.util.debugger
import sqip.InAppPaymentsSdk
import timber.log.Timber

/**
 * [Readium] is the main entry point of the application
 */
class Readium : Application() {

    override fun onCreate() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            enableStrictMode()
        }
        super.onCreate()

        // Enable Bugsnag
        Bugsnag.init(this@Readium)

        // Enable Firebase
        FirebaseApp.initializeApp(this@Readium).apply {
            debugger("Application started successfully")
        }

        // In-App payment
        InAppPaymentsSdk.squareApplicationId = BuildConfig.SQUARE_APP_ID
    }


    // Thread policy implementation
    private fun enableStrictMode() = StrictMode.ThreadPolicy.Builder()
        .detectAll()
        .penaltyLog()
        .build()

}