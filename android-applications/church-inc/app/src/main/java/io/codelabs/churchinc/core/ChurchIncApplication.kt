package io.codelabs.churchinc.core

import android.app.Application
import io.codelabs.churchinc.core.modules.firebaseModule
import io.codelabs.churchinc.core.modules.roomModule
import org.koin.android.ext.android.startKoin

/**
 * [Application] subclass
 */
class ChurchIncApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // INITIALIZE KOIN HERE
        startKoin(
            this@ChurchIncApplication, modules = listOf(firebaseModule, roomModule)
        )

    }
}