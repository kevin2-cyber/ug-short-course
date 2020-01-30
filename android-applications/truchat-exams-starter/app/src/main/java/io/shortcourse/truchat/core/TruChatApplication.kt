package io.shortcourse.truchat.core

import android.app.Application
import io.shortcourse.truchat.core.injection.roomModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TruChatApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Koin here
        startKoin {
            androidContext(this@TruChatApplication)
            modules(roomModule)
        }
    }
}