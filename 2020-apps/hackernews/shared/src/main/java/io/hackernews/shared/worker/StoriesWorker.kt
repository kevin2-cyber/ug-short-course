package io.hackernews.shared.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import io.hackernews.shared.database.HackerNewsDatabase
import io.hackernews.shared.database.StoryDao
import io.hackernews.shared.debugger
import io.hackernews.shared.network.NewsConfig

/**
 * Pre-loads all stories for the user
 */
class StoriesWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    private val newsService by lazy { NewsConfig.getService() }
    private val dao: StoryDao by lazy { HackerNewsDatabase.getInstance(context).storyDao() }

    override suspend fun doWork(): Result {
        return try {
            val topStoriesIds = newsService.getTopStoriesIds() ?: return Result.failure()

            // Load first twenty
            topStoriesIds.subList(0, 20).forEach { storyId ->
                val story = newsService.getStory(storyId)
                if (story != null) {
                    dao.insert(story)
                }
            }

            Result.success()
        } catch (ex: Exception) {
            debugger(ex.localizedMessage)
            Result.retry()
        }
    }

}