/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.shared.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import dev.ugscheduler.shared.data.Course
import dev.ugscheduler.shared.datasource.local.CourseDao
import dev.ugscheduler.shared.datasource.local.LocalDatabase
import dev.ugscheduler.shared.datasource.remote.courseDocument
import dev.ugscheduler.shared.util.Constants
import dev.ugscheduler.shared.util.debugger
import dev.ugscheduler.shared.util.deserializer.getCourses
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Worker for handling [Course] information retrieval from the remote database to the local database
 */
class CourseWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    private val dao: CourseDao by lazy { LocalDatabase.get(context).courseDao() }
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    override suspend fun doWork(): Result {
        debugger("De-serializing and adding courses")

        // Get all courses and deserialize
        val courses = getCourses(applicationContext)

        // Needed to be called on the background thread
        withContext(Dispatchers.IO) {
            // Store locally
            dao.insertAll(courses)

            // Forward all course details to remote data source
            for (course in courses) {
                try {
                    Tasks.await(
                        firestore.courseDocument(course.id).set(
                            course,
                            SetOptions.merge()
                        )
                    )
                } catch (e: Exception) {
                    debugger("Adding courses exception: ${e.localizedMessage}")
                }
            }
        }
        return Result.success()
    }
}