package io.codelabs.ugcloudchat.core

import android.app.Application
import com.google.firebase.FirebaseApp
import io.codelabs.ugcloudchat.BuildConfig
import io.codelabs.ugcloudchat.util.debugThis
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Application's entry point
 */
class WhatsappApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase SDK
        FirebaseApp.initializeApp(this).apply {
            debugThis(this?.name)
        }

        // Initialize Koin
        startKoin {
            androidContext(this@WhatsappApplication)
            androidLogger(if (BuildConfig.DEBUG) Level.DEBUG else Level.INFO)
            modules(
                arrayListOf(
                    prefsModule,
                    roomModule,
                    firebaseModule,
                    providerModule,
                    androidModule
                )
            )
        }
    }

}