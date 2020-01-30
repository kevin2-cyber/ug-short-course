package io.hackernews

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import io.hackernews.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        findNavController(R.id.nav_host_fragment).addOnDestinationChangedListener { _, destination, arguments ->
            when (destination.id) {
                R.id.nav_stories -> {
                }
                R.id.nav_story_details -> {
                }
            }
        }

    }
}