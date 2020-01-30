package io.hackernews

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import io.hackernews.databinding.ActivityMainBinding
import io.hackernews.shared.base.BaseActivity
import io.hackernews.shared.database.HackerNewsDatabase

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.isLoadingNews = true
        loadNewsContent()
    }

    private fun loadNewsContent() {
        HackerNewsDatabase.getInstance(this@MainActivity)
            .storyDao().getAllStories().observe(this@MainActivity, Observer { stories ->
                binding.isLoadingNews = stories.isEmpty()
                binding.executePendingBindings()
            })
    }

    fun navToHome(view: View) {
        startActivity(Intent(this@MainActivity, HomeActivity::class.java))
        finishAfterTransition()
    }
}