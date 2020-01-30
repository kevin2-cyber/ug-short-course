package dev.codelabs.samplenewsapp.datasource

import dev.codelabs.samplenewsapp.ApiResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WebService {
    companion object {
        const val BASE_URL = "https://newsapi.org/"
        const val API_KEY = "7a17d0dc7f5b450289fde5f3ab24f4d5"
    }

    @GET("/v2/top-headlines")
    suspend fun getNews(
        @Query("sources") source: String,
        @Query("apiKey") key: String = API_KEY
    ): ApiResponse

}

object WebServiceProvider {
    fun getService(): WebService {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    println("SampleNews =>  $message")
                }
            })).build()

        return Retrofit.Builder()
            .baseUrl(WebService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WebService::class.java)
    }
}