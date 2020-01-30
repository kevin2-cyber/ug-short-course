package dev.csshortcourse.ugnews.datasource

import androidx.lifecycle.LiveData
import dev.csshortcourse.ugnews.model.NewsArticle

interface DataSource {
    suspend fun getNews(source: String): LiveData<MutableList<NewsArticle>>
}