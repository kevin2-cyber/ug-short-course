/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package io.codelabs.githubrepo.shared.datasource.remote

import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import io.codelabs.githubrepo.shared.BuildConfig
import io.codelabs.githubrepo.shared.BuildConfig.DEBUG
import io.codelabs.githubrepo.shared.core.prefs.AppSharedPreferences
import io.codelabs.githubrepo.shared.data.Owner
import io.codelabs.githubrepo.shared.data.Repo
import io.codelabs.githubrepo.shared.repository.AuthRepository
import io.codelabs.githubrepo.shared.util.Constants
import io.codelabs.githubrepo.shared.util.debugger
import kotlinx.coroutines.Deferred
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.io.File
import java.util.concurrent.TimeUnit

interface GitHubService {
    companion object {
        const val BASE_URL = BuildConfig.API_BASE_URL
    }

    @GET("/user")
    fun getUserAsync(): Deferred<Owner>

    // Get all repositories
    @GET("/repositories")
    fun getAllReposAsync(): Deferred<MutableList<Repo>>
}

/**
 * Rest API creation
 */
object RepoAPI {

    // Create service for making calls to GitHub API
    fun createService(context: Context): GitHubService {
        // Get client
        val client: OkHttpClient = createClient(context)

        // Create retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(GitHubService.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        /// Create service with params above
        return retrofit.create(GitHubService::class.java)
    }

    private fun createClient(context: Context): OkHttpClient {
        // Create cache
        val cache =
            Cache(
                File(context.cacheDir.absolutePath, Constants.CACHE_DIR),
                Constants.MAX_CACHE_SIZE
            )

        // Add interceptor for calls to enable debugging
        val interceptor: Interceptor =
            HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    debugger(message)
                }
            }).apply {
                level =
                    if (DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level./*NONE*/BODY
            }

        // Create client
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("Authorization", "Bearer ${AppSharedPreferences.get(context).token!!}")
                    .method(original.method, original.body)
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(interceptor)
            .connectTimeout(120L, TimeUnit.SECONDS)
            .callTimeout(30L, TimeUnit.SECONDS)
            .cache(cache).build()
    }

    fun createAuthService(context: Context): AuthRepository {
        // Get client
        val client: OkHttpClient = createClient(context)

        // Create retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(AuthRepository.AUTH_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        /// Create service with params above
        return retrofit.create(AuthRepository::class.java)
    }

}