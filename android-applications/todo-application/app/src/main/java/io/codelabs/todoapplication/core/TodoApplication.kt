package io.codelabs.todoapplication.core

import android.app.Application
import io.codelabs.todoapplication.module.appModule
import io.codelabs.todoapplication.module.roomModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * [Application] class
 */
class TodoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Start Koin
        startKoin {
            androidContext(this@TodoApplication)

            modules(roomModule, appModule)
        }
    }
}