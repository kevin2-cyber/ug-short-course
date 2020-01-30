package dev.csshortcourse.ugnews.repository

import androidx.lifecycle.LiveData
import dev.csshortcourse.ugnews.datasource.DataSource
import dev.csshortcourse.ugnews.model.NewsArticle
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

interface Repository {
    suspend fun getNews(source: String, refresh: Boolean): LiveData<MutableList<NewsArticle>>
}

class NewsRepository(private val dataSource: DataSource) : Repository {
    override suspend fun getNews(
        source: String,
        refresh: Boolean
    ): LiveData<MutableList<NewsArticle>> {
        return withContext(IO) {
            dataSource.getNews(source)
        }
    }
}