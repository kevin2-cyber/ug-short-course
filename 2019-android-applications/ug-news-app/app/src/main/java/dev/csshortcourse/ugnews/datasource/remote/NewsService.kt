package dev.csshortcourse.ugnews.datasource.remote

import dev.csshortcourse.ugnews.BuildConfig
import dev.csshortcourse.ugnews.util.debugger
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

/**
 * News service API builder
 */
class NewsService private constructor() {
    private class Builder {
        private fun getService(): Retrofit {
            val client: OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                    override fun log(message: String) {
                        debugger(message)
                    }
                }).apply { level = HttpLoggingInterceptor.Level.HEADERS })
                .cache(Cache(File("news-org-cache"), 10.times(1024).times(1024)))
                .build()
            return Retrofit.Builder()
                .baseUrl(BuildConfig.NEWS_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun build(): NewsAPI {
            return getService().create(NewsAPI::class.java)
        }
    }

    companion object {
        @Volatile
        private var instance: NewsAPI? = null

        fun get(): NewsAPI = instance ?: synchronized(this) {
            instance ?: Builder().build().also { instance = it }
        }
    }
}