package shortcourse.readium.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import shortcourse.readium.R
import shortcourse.readium.core.base.BaseActivity
import shortcourse.readium.databinding.MainActivityBinding
import shortcourse.readium.view.fragment.HomeFragmentDirections

class MainActivity : BaseActivity() {
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)

        binding.run {
            setSupportActionBar(bottomAppBar)
            fabBottomAppBar.setOnClickListener {
                findNavController(R.id.nav_host_fragment)
                    .navigate(HomeFragmentDirections.actionNavHomeToNavCompose())
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_app_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_search -> findNavController(R.id.nav_host_fragment)
                .navigate(HomeFragmentDirections.actionNavHomeToNavSearch())

            R.id.nav_settings -> findNavController(R.id.nav_host_fragment)
                .navigate(HomeFragmentDirections.actionNavHomeToNavSettings())
        }
        return super.onOptionsItemSelected(item)
    }

}