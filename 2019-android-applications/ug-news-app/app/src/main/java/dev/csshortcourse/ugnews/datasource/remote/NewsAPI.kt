package dev.csshortcourse.ugnews.datasource.remote

import dev.csshortcourse.ugnews.BuildConfig
import dev.csshortcourse.ugnews.model.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {
    @GET("/v2/top-headlines")
    suspend fun getNews(
        @Query("sources") source: String,
        @Query("apiKey") key: String = BuildConfig.API_KEY
    ): ApiResponse
}