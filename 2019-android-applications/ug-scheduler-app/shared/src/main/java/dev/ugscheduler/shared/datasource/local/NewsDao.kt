/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.shared.datasource.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import dev.ugscheduler.shared.data.News

@Dao
interface NewsDao : BaseDao<News> {

    @Query("select * from news order by timestamp desc")
    fun getNewsArticles(): LiveData<MutableList<News>>

    @Query("select * from news order by timestamp desc")
    fun getNewsArticlesAsync(): MutableList<News>
}