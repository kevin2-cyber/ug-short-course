package io.hackernews.shared

import android.app.Application
import android.os.StrictMode
import io.hackernews.shared.BuildConfig.DEBUG
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

/**
 * HackerNews [Application] subclass
 * Used to instantiate any global process or module or service
 * Handles dependency injection initialization
 */
class HackerNews : Application() {
    override fun onCreate() {
        if (DEBUG) {
            enableStrictMode()
            Timber.plant(Timber.DebugTree())
        }
        super.onCreate()
        debugger("Starting HackerNews application...")

        // Dependency Injection
        startKoin {
            androidContext(this@HackerNews)
            // TODO: 1/29/2020 Add modules here
            modules(mutableListOf())
        }
    }

    private fun enableStrictMode() = StrictMode.ThreadPolicy.Builder()
        .detectDiskReads()
        .detectDiskWrites()
        .detectNetwork()
        .detectCustomSlowCalls()
        .detectResourceMismatches()
        .penaltyLog()
        .build()
}