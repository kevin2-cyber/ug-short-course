/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package io.codelabs.githubrepo.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import io.codelabs.githubrepo.R
import io.codelabs.githubrepo.databinding.ActivityMainBinding
import io.codelabs.githubrepo.shared.BuildConfig
import io.codelabs.githubrepo.shared.core.base.BaseActivity
import io.codelabs.githubrepo.shared.core.prefs.AppSharedPreferences
import io.codelabs.githubrepo.shared.repository.AuthRepository
import io.codelabs.githubrepo.shared.util.debugger
import io.codelabs.githubrepo.shared.util.intentTo
import io.codelabs.githubrepo.shared.util.toast
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import java.util.*

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private val prefs by inject<AppSharedPreferences>()
    private val redirectUrl = "gitrepo://callback"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("CAST_NEVER_SUCCEEDS")
        binding = DataBindingUtil.setContentView<ViewDataBinding>(
            this,
            R.layout.activity_main
        ) as ActivityMainBinding

        // Listen for live events of user login
        prefs.liveLoginStatus.observe(this, Observer { state ->
            if (state) {
                // Navigates users to home screen when they are logged
                intentTo(HomeActivity::class.java, true)
            }
        })

        onNewIntent(intent)
    }

    fun login(view: View) {
        val authUrl =
            "https://github.com/login/oauth/authorize?client_id=${BuildConfig.API_KEY}&scope=repo,user&redirect_uri=$redirectUrl&allow_signup=true"
        startActivity(Intent(Intent.ACTION_VIEW, authUrl.toUri()))
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null && intent.data != null && intent.data.toString().startsWith(redirectUrl)) {
            val code = intent.data?.getQueryParameter("code")
            debugger(code)
            val authRepo: AuthRepository = get()
            ioScope.launch {
                val accessToken = authRepo.getAccessTokenAsync(code.toString()).await()

                // Get access token
                debugger(accessToken)

                uiScope.launch {
                    // Sign in user
                    prefs.login(UUID.randomUUID().toString(), accessToken.token)
                    toast("Logged in successfully")
                }
            }
        } else debugger("No intent data received")
    }
}
