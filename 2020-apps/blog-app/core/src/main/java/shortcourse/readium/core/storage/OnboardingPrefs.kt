package shortcourse.readium.core.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import shortcourse.readium.core.util.StorageUtil

/**
 * Maintains the state of the onboarding process
 */
class OnboardingPrefs private constructor(context: Context) {

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(
            StorageUtil.ONBOARDING_PREFS,
            Context.MODE_PRIVATE
        )
    }

    private val _liveState = MutableLiveData<Boolean>(false)

    val currentState: LiveData<Boolean> get() = _liveState

    init {
        _liveState.value = prefs.getBoolean(StorageUtil.Keys.ONBOARDING_STATE, false)
    }

    /**
     * Complete the onboarding process
     */
    fun completeOnboarding() {
        prefs.edit {
            putBoolean(StorageUtil.Keys.ONBOARDING_STATE, true)
            apply()
        }
        _liveState.value = true
    }

    /**
     * Restart the onboarding process
     */
    fun reset() {
        prefs.edit {
            putBoolean(StorageUtil.Keys.ONBOARDING_STATE, false)
            apply()
        }
        _liveState.value = false
    }

    companion object {
        @Volatile
        private var instance: OnboardingPrefs? = null

        fun getInstance(context: Context): OnboardingPrefs = instance ?: synchronized(this) {
            instance ?: OnboardingPrefs(context).also { instance = it }
        }
    }

}