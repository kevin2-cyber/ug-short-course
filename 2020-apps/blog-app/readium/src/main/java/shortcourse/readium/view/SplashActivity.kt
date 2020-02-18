package shortcourse.readium.view

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.intentFor
import shortcourse.readium.R
import shortcourse.readium.core.base.BaseActivity
import shortcourse.readium.databinding.ActivitySplashBinding

/**
 * Welcome screen for application
 */
class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Apply data binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        uiScope.launch {
            delay(3500)
            startActivity(intentFor<MainActivity>())
            finishAfterTransition()
        }

    }
}