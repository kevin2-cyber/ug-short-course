package dev.codelabs.samplenewsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dev.codelabs.samplenewsapp.recyclerview.NewsAdapter
import dev.codelabs.samplenewsapp.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsActivity : AppCompatActivity() {
    private lateinit var viewModel: NewsViewModel
    private val uiScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        viewModel = NewsViewModel(application)

        val newsAdapter = NewsAdapter()

        news_list.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(this@NewsActivity)
            setHasFixedSize(false)
        }

        uiScope.launch {
            viewModel.getNews("fox-news").observe(this@NewsActivity, Observer { news ->
                newsAdapter.submitList(news)
                println("SampleNews => ${news.size}")
            })
        }

    }
}
