/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.ui.onboarding

import androidx.lifecycle.ViewModel
import dev.ugscheduler.shared.util.BaseActivity
import dev.ugscheduler.shared.util.intentTo
import dev.ugscheduler.ui.HomeActivity

class OnboardingViewModel : ViewModel() {

    fun showOnboarding(host: BaseActivity): Boolean =
        OnboardingPreferences(host).getOnboardingState()

    fun performOnboardingCompletion(host: BaseActivity) {
        val prefs = OnboardingPreferences(host)
        prefs.completeOnboarding()
        host.intentTo(HomeActivity::class.java, true)
    }
}
