/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.ui

import android.os.Bundle
import dev.ugscheduler.R
import dev.ugscheduler.databinding.ActivityWelcomeBinding
import dev.ugscheduler.shared.util.BaseActivity
import dev.ugscheduler.ui.onboarding.OnboardingFragment

class WelcomeActivity : BaseActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Replace container with onboarding fragment
        supportFragmentManager.beginTransaction().replace(
            R.id.frame_container,
            OnboardingFragment(),
            OnboardingFragment::class.java.simpleName
        ).commit()
    }
}
