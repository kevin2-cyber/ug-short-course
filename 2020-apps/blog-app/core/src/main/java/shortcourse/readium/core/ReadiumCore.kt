package shortcourse.readium.core

import android.app.Application
import android.os.StrictMode
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import shortcourse.readium.core.util.debugger
import timber.log.Timber

/**
 * Base [Application] class that allows for dependency injection using Koin
 */
open class ReadiumCore : Application() {

    override fun onCreate() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            enableStrictMode()
        }
        super.onCreate()

        // Enable Firebase
        FirebaseApp.initializeApp(this@ReadiumCore).apply {
            debugger("Application started successfully")
        }

        // Dependency injection using Koin
        startKoin {
            androidContext(this@ReadiumCore)
            modules(mutableListOf())
        }
    }


    // Thread policy implementation
    private fun enableStrictMode() = StrictMode.ThreadPolicy.Builder()
        .detectAll()
        .penaltyLog()
        .build()

}