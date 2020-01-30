package dev.csshortcourse.ugnews.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.csshortcourse.ugnews.datasource.remote.NewsService
import dev.csshortcourse.ugnews.model.NewsArticle
import dev.csshortcourse.ugnews.util.debugger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class NewsDataSource : DataSource {
    private val api by lazy { NewsService.get() }

    override suspend fun getNews(source: String): LiveData<MutableList<NewsArticle>> {
        val results = MutableLiveData<MutableList<NewsArticle>>()
        try {
            val news = api.getNews(source)
            debugger(news.results)
            CoroutineScope(Main).launch {
                results.value = news.articles
            }
        } catch (e: Exception) {
            debugger(e.localizedMessage)
        }
        return results
    }
}