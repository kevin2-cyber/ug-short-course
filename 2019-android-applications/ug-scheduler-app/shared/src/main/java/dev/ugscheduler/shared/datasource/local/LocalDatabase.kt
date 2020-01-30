/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.shared.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dev.ugscheduler.shared.data.*
import dev.ugscheduler.shared.util.Constants
import dev.ugscheduler.shared.util.debugger
import dev.ugscheduler.shared.worker.CourseWorker
import dev.ugscheduler.shared.worker.FacilitatorWorker
import dev.ugscheduler.shared.worker.NewsWorker

@Database(
    entities = [Student::class, Facilitator::class, Course::class, Feedback::class, News::class],
    version = Constants.DB_VERSION,
    exportSchema = true
)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun studentDao(): StudentDao
    abstract fun courseDao(): CourseDao
    abstract fun feedbackDao(): FeedbackDao
    abstract fun facilitatorDao(): FacilitatorDao
    abstract fun newsDao(): NewsDao

    companion object {
        @Volatile
        private var instance: LocalDatabase? = null

        fun get(context: Context): LocalDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context,
                LocalDatabase::class.java,
                Constants.LOCAL_DB_NAME
            )
                .fallbackToDestructiveMigration()
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        debugger("Creating new database")

                        // Set a one-time request to download all courses and store them locally
                        with(WorkManager.getInstance(context)) {
                            enqueue(
                                mutableListOf(
                                    OneTimeWorkRequestBuilder<FacilitatorWorker>().build(),
                                    OneTimeWorkRequestBuilder<CourseWorker>().build(),
                                    OneTimeWorkRequestBuilder<NewsWorker>().build()
                                )
                            )
                        }
                    }
                })
                .build().also { instance = it }
        }
    }
}

