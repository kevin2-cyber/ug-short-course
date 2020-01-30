package io.hackernews.shared.network

import io.hackernews.shared.data.Story
import retrofit2.http.GET
import retrofit2.http.Path

interface NewsService {
    companion object {
        const val BASE_URL = " https://hacker-news.firebaseio.com/"
    }

    @GET("/v0/topstories.json?print=pretty")
    suspend fun getTopStoriesIds(): List<Int>?

    @GET("/v0/item/{id}.json?print=pretty")
    suspend fun getStory(@Path("id") id: Int): Story?
}