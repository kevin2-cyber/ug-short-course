package shortcourse.readium.core.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import shortcourse.readium.core.storage.OnboardingPrefs

class OnboardingViewModel(private val onboardingPrefs: OnboardingPrefs) : ViewModel() {

    private val _liveState = MutableLiveData<Boolean>()
    val currentState: LiveData<Boolean> get() = _liveState

    init {
        _liveState.value = onboardingPrefs.shouldShowOnboarding
    }

    fun completeOnboarding() {
        onboardingPrefs.completeOnboarding()
        _liveState.value = onboardingPrefs.shouldShowOnboarding
    }

}