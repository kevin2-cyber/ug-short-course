/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler

import android.app.Application
import android.os.StrictMode
import dev.ugscheduler.BuildConfig.DEBUG
//import dev.ugscheduler.BuildConfig.DEBUG
import dev.ugscheduler.shared.di.loadAppModules
import dev.ugscheduler.shared.notification.NotificationUtils
import dev.ugscheduler.shared.prefs.AppPreferences
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {
    private val notificationUtils by lazy { get<NotificationUtils>() }

    override fun onCreate() {
        if (DEBUG) {
            enableStrictMode()
            Timber.plant(Timber.DebugTree())
        }
        super.onCreate()

        // Register dependencies
        startKoin {
            androidContext(this@App)
            modules(loadAppModules())
        }

        // Set default theme
        with(get<AppPreferences>()) {
            setDarkMode(currentMode)
        }

        // Register notification channels
        notificationUtils.registerChannels()
    }

    private fun enableStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build()
        )
    }

    override fun onTerminate() {
        super.onTerminate()
        notificationUtils.dismissAllNotifications()
    }
}