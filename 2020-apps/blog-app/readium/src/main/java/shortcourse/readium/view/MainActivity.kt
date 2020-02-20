package shortcourse.readium.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import org.koin.android.ext.android.inject
import shortcourse.readium.R
import shortcourse.readium.core.base.BaseActivity
import shortcourse.readium.core.storage.AccountPrefs
import shortcourse.readium.databinding.MainActivityBinding
import shortcourse.readium.view.fragment.CommentFragmentDirections
import shortcourse.readium.view.fragment.HomeFragmentDirections
import shortcourse.readium.view.fragment.PostFragmentDirections
import shortcourse.readium.view.fragment.SettingsFragmentDirections

class MainActivity : BaseActivity() {
    private lateinit var binding: MainActivityBinding
    private lateinit var controller: NavController
    private val prefs: AccountPrefs by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)

        // NavController
        controller = findNavController(R.id.nav_host_fragment)

        // Data binding
        binding.run {
            setSupportActionBar(bottomAppBar)
            executePendingBindings()
        }

        // Listen for destination change events
        controller.addOnDestinationChangedListener { _, destination, _ ->
            setupBottomAppBarForDestination(destination)
        }

        if (!prefs.isLoggedIn) controller.navigate(R.id.nav_auth)
    }

    private fun setupBottomAppBarForDestination(navDestination: NavDestination) {
        when (navDestination.id) {
            R.id.nav_account, R.id.nav_search, R.id.nav_auth,
            R.id.nav_onboarding, R.id.nav_registration, R.id.nav_compose -> {
                binding.run {
                    bottomAppBar.performHide()
                    fabBottomAppBar.hide()
                }
            }

            R.id.nav_post, R.id.nav_settings, R.id.nav_comment -> {
                binding.run {
                    bottomAppBar.performHide()
                    fabBottomAppBar.setOnClickListener {
                        val direction: NavDirections = when (navDestination.id) {
                            R.id.nav_post -> PostFragmentDirections.actionNavPostToNavCompose(null)
                            R.id.nav_settings -> SettingsFragmentDirections.actionNavSettingsToNavCompose(
                                null
                            )
                            R.id.nav_comment -> CommentFragmentDirections.actionNavCommentToNavCompose(
                                null
                            )
                            else -> HomeFragmentDirections.actionNavHomeToNavCompose(null)
                        }
                        controller.navigate(direction)
                    }
                }
            }

            else -> {
                binding.run {
                    bottomAppBar.performShow()
                    fabBottomAppBar.show()
                    fabBottomAppBar.setOnClickListener {
                        controller.navigate(HomeFragmentDirections.actionNavHomeToNavCompose(null))
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_app_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_search -> controller.navigate(HomeFragmentDirections.actionNavHomeToNavSearch())

            R.id.nav_settings -> controller.navigate(HomeFragmentDirections.actionNavHomeToNavSettings())
        }
        return super.onOptionsItemSelected(item)
    }

}