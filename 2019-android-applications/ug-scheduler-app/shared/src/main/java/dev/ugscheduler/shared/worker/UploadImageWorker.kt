/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.shared.worker

import android.content.Context
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dev.ugscheduler.shared.datasource.local.LocalDataSource
import dev.ugscheduler.shared.datasource.local.LocalDatabase
import dev.ugscheduler.shared.datasource.remote.RemoteDataSource
import dev.ugscheduler.shared.util.Constants
import dev.ugscheduler.shared.util.debugger
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class UploadImageWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
    private val bucket by lazy {
        FirebaseStorage.getInstance().reference.child(Constants.BUCKET_NAME)
    }
    private val remoteDataSource by lazy {
        RemoteDataSource(
            FirebaseFirestore.getInstance(),
            FirebaseAuth.getInstance()
        )
    }
    private val database by lazy { LocalDatabase.get(context) }
    private val localDataSource by lazy { LocalDataSource(database) }

    override suspend fun doWork(): Result {
        return try {
            uploadImage(inputData.getString("id"), inputData.getString("uri"))
            Result.success()
        } catch (ex: Exception) {
            debugger(ex.localizedMessage)
            Result.retry()
        }
    }

    private suspend fun uploadImage(id: String?, uri: String?) {
        debugger("Uploading image from worker with id $id and uri  => $uri")
        if (uri.isNullOrEmpty() || id.isNullOrEmpty()) return
        withContext(IO) {
            // Download url for profile image
            val downloadUri =
                Tasks.await(Tasks.await(bucket.putFile(uri.toUri())).storage.downloadUrl).toString()

            // Get current student
            val student = localDataSource.database.studentDao().getStudentById(id)

            // Store information locally and remotely
            localDataSource.database.studentDao().insert(student.copy(avatar = downloadUri))
            remoteDataSource.addStudent(student.copy(avatar = downloadUri))
        }
    }
}