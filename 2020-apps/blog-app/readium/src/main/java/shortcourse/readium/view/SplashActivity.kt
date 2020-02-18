package shortcourse.readium.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import shortcourse.readium.R
import shortcourse.readium.databinding.ActivitySplashBinding
import shortcourse.readium.util.debugger

/**
 * Welcome screen for application
 */
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Apply data binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        binding.run {
            if (!animationView.isAnimating) {
                debugger("Animation has stopped")
            }
            executePendingBindings()
        }

    }
}