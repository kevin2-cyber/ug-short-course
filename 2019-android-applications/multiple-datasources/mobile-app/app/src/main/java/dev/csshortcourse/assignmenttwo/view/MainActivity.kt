package dev.csshortcourse.assignmenttwo.view

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.transition.TransitionManager
import dev.csshortcourse.assignmenttwo.databinding.ActivityMainBinding
import dev.csshortcourse.assignmenttwo.util.BaseActivity
import dev.csshortcourse.assignmenttwo.util.moveTo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * This assignment is mainly focused on working with multiple data sources in an application
 * This is a simple chat application (local and remote)
 *
 * Remote data source: NodeJS
 * Local data source: Room
 */
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Animate button into view
        uiScope.launch {
            delay(1250)
            TransitionManager.beginDelayedTransition(binding.container/*, Slide(Gravity.END)*/)
            prefs.liveLoginState.observe(this@MainActivity, Observer {
                if (it)
                    moveTo(HomeActivity::class.java, true)
                else
                    moveTo(AuthActivity::class.java, true)
            })
        }
    }
}
