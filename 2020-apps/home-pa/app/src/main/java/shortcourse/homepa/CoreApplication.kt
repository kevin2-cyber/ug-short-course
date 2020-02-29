package shortcourse.homepa

import android.app.Application
import com.google.firebase.FirebaseApp

/**
 * [Application] subclass
 */
class CoreApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
    }

}