/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package io.codelabs.githubrepo.ui

import android.os.Bundle
import io.codelabs.githubrepo.R
import io.codelabs.githubrepo.shared.core.base.BaseActivity
import io.codelabs.githubrepo.shared.core.prefs.AppSharedPreferences
import io.codelabs.githubrepo.shared.repository.GitHubRepository
import io.codelabs.githubrepo.shared.util.debugger
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class HomeActivity : BaseActivity() {
    //private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val repo: GitHubRepository = get()
        val prefs: AppSharedPreferences = get()
        ioScope.launch {
            debugger(prefs.token)
            try {
                val owner = repo.requestUserIdentity()
                debugger("Owner created as: $owner")
            } catch (e: Exception) {
                debugger(e.localizedMessage)
            }
        }
    }

}