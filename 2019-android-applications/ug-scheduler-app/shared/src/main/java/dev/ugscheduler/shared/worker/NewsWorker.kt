/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.shared.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dev.ugscheduler.shared.datasource.local.LocalDataSource
import dev.ugscheduler.shared.datasource.local.LocalDatabase
import dev.ugscheduler.shared.datasource.remote.RemoteDataSource
import dev.ugscheduler.shared.util.debugger
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

/**
 * Loads and caches all news articles offline for first time usage
 */
class NewsWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    private val remoteDataSource by lazy {
        RemoteDataSource(
            FirebaseFirestore.getInstance(),
            FirebaseAuth.getInstance()
        )
    }
    private val localDataSource by lazy { LocalDataSource(LocalDatabase.get(context)) }

    override suspend fun doWork(): Result {
        debugger("Adding news articles")
        return try {
            withContext(IO) {
                // Get news articles
                val newsArticles = remoteDataSource.getNewsArticles()

                // Store articles locally
                localDataSource.database.newsDao().insertAll(newsArticles)
            }
            debugger("News articles retrieved successfully")
            Result.success()
        } catch (e: Exception) {
            debugger("Unable to retrieve news articles at this time")
            Result.retry()
        }
    }

}