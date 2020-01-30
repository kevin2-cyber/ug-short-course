package dev.codelabs.samplenewsapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.codelabs.samplenewsapp.NewsArticle
import dev.codelabs.samplenewsapp.datasource.NewsDataSource
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

interface Repository {
    suspend fun getNews(source: String): LiveData<MutableList<NewsArticle>>
}

class NewsRepository(private val dataSource: NewsDataSource) : Repository {
    override suspend fun getNews(source: String): LiveData<MutableList<NewsArticle>> = dataSource.getNews(source)
}