package dev.codelabs.samplenewsapp.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.codelabs.samplenewsapp.NewsArticle

interface DataSource {
    suspend fun getNews(source: String): LiveData<MutableList<NewsArticle>>
}

class NewsDataSource : DataSource {
    private val serviceApi by lazy { WebServiceProvider.getService() }

    override suspend fun getNews(source: String): LiveData<MutableList<NewsArticle>> {
        val liveNews = MutableLiveData<MutableList<NewsArticle>>()
        try {
            val news = serviceApi.getNews(source)
            liveNews.postValue(news.articles)
        } catch (e: Exception) {
            println("SampleNews => ${e.localizedMessage}")
        }
        return liveNews
    }
}