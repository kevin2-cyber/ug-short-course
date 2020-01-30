/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package io.codelabs.githubrepo.shared.core

import com.google.firebase.auth.FirebaseAuth
import io.codelabs.githubrepo.shared.core.prefs.AppSharedPreferences
import io.codelabs.githubrepo.shared.datasource.local.RepoDatabase
import io.codelabs.githubrepo.shared.datasource.remote.RepoAPI
import io.codelabs.githubrepo.shared.repository.GitHubRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

fun loadModules() = mutableListOf(firebaseModule, networkModule, prefsModule)

private val firebaseModule = module {
    single { FirebaseAuth.getInstance() }
}

private val networkModule = module {
    // Remote datasource
    single { RepoAPI.createService(androidContext()) }
    single { RepoAPI.createAuthService(androidContext()) }

    // Local datasource
    single { RepoDatabase.get(androidContext()) }
    single { get<RepoDatabase>().ownerDao() }
    single { get<RepoDatabase>().repoDao() }

    // Repositories
    single { GitHubRepository(get(), get()) }
}

private val prefsModule = module {
    single { AppSharedPreferences.get(androidContext()) }
}