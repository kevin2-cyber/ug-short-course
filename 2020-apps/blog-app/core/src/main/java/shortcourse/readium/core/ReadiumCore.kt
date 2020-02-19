package shortcourse.readium.core

import android.app.Application
import android.os.StrictMode
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import shortcourse.readium.core.koin.injectables
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

        // Dependency injection using Koin
        startKoin {
            androidContext(this@ReadiumCore)
            modules(injectables)
        }
    }


    // Thread policy implementation
    private fun enableStrictMode() = StrictMode.ThreadPolicy.Builder()
        .detectAll()
        .penaltyLog()
        .build()

}