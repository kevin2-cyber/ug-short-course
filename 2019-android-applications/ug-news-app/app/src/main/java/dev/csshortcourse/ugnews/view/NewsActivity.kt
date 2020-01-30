package dev.csshortcourse.ugnews.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dev.csshortcourse.ugnews.R
import dev.csshortcourse.ugnews.databinding.ActivityNewsBinding
import dev.csshortcourse.ugnews.view.recyclerview.NewsAdapter
import dev.csshortcourse.ugnews.viewmodel.NewsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    private val viewModel by viewModels<NewsViewModel>()
    private var currentSource: String = "techcrunch"
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Adapter
        newsAdapter = NewsAdapter()

        // Swipe to refresh
        binding.swipeRefresh.setOnRefreshListener {
            fetchData()
        }

        // RecyclerView
        binding.newsList.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(this@NewsActivity)
            itemAnimator = DefaultItemAnimator()
            setHasFixedSize(false)
        }

        // ChipGroup
        binding.sourceGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.cnbc -> {
                    currentSource = "cnbc"
                    fetchData()
                }

                R.id.abc_news -> {
                    currentSource = "abc-news"
                    fetchData()
                }

                R.id.techcrunch -> {
                    currentSource = "techcrunch"
                    fetchData()
                }
            }
        }

        // Fetch news from source
        fetchData()
    }

    private fun fetchData() {
        binding.swipeRefresh.isRefreshing = true
        CoroutineScope(Main).launch {
            viewModel.getNews(currentSource).observe(this@NewsActivity, Observer { news ->
                newsAdapter.submitList(news)
                binding.swipeRefresh.isRefreshing = false
                Snackbar.make(binding.container, "Refresh completed", Snackbar.LENGTH_SHORT).show()
            })
        }
    }

}
