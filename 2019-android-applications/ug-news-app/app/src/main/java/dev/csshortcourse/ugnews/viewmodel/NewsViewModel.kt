package dev.csshortcourse.ugnews.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.csshortcourse.ugnews.datasource.NewsDataSource
import dev.csshortcourse.ugnews.model.NewsArticle
import dev.csshortcourse.ugnews.repository.NewsRepository
import kotlinx.coroutines.launch

class NewsViewModel(app: Application) : AndroidViewModel(app) {
    private val repo by lazy { NewsRepository(NewsDataSource()) }
    private val _news = MutableLiveData<MutableList<NewsArticle>>()

    fun getNews(source: String): LiveData<MutableList<NewsArticle>> {
        viewModelScope.launch {
            val liveNews = repo.getNews(source, true)
            _news.postValue(liveNews.value)
        }
        return _news
    }

}