package dev.codelabs.samplenewsapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import dev.codelabs.samplenewsapp.NewsArticle
import dev.codelabs.samplenewsapp.datasource.NewsDataSource
import dev.codelabs.samplenewsapp.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NewsViewModel(app: Application) : AndroidViewModel(app) {
    private val repo by lazy { NewsRepository(NewsDataSource()) }

    suspend fun getNews(source: String): LiveData<MutableList<NewsArticle>> {
        return withContext(Dispatchers.Main) {
            val liveData = repo.getNews(source)
            liveData
        }
    }

}