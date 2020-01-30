/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.ui.onboarding

import android.content.Context
import androidx.core.content.edit

class OnboardingPreferences constructor(context: Context) {

    private val prefs by lazy {
        context.getSharedPreferences(
            "onboarding_prefs",
            Context.MODE_PRIVATE
        )
    }

    fun getOnboardingState(): Boolean = prefs.getBoolean("key_onboarding", false)

    fun completeOnboarding() {
        prefs.edit {
            putBoolean("key_onboarding", true)
            apply()
        }
    }

}