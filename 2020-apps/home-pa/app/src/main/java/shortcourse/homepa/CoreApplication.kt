package shortcourse.homepa

import android.app.Application
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import shortcourse.homepa.injection.firebaseModule

/**
 * [Application] subclass
 */
class CoreApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Dependency Injection using Koin
        startKoin {
            androidContext(this@CoreApplication)
            modules(mutableListOf(firebaseModule))
        }
    }

}