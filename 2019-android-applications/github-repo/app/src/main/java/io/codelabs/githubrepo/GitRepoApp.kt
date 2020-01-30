/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package io.codelabs.githubrepo

import android.app.Application
import com.google.firebase.FirebaseApp
import io.codelabs.githubrepo.BuildConfig.DEBUG
import io.codelabs.githubrepo.shared.core.loadModules
import io.codelabs.githubrepo.shared.util.AppCustomTree
import io.codelabs.githubrepo.shared.util.debugger
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

/**
 * Entry point to the application
 */
class GitRepoApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Firebase
        FirebaseApp.initializeApp(this).apply {
            debugger("Firebase initialized as: ${this?.name}")
        }

        // Timber
//        if (DEBUG) {
            Timber.plant(Timber.DebugTree())
//        } else {
//            Timber.plant(AppCustomTree())
//        }

        // Koin DSL for dependency injection
        startKoin {
            androidContext(this@GitRepoApp)
            modules(loadModules())
        }
    }

}