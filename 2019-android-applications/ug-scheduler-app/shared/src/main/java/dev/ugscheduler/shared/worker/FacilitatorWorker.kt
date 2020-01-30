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
import dev.ugscheduler.shared.data.Facilitator
import dev.ugscheduler.shared.datasource.local.LocalDatabase
import dev.ugscheduler.shared.datasource.remote.facilitatorDocument
import dev.ugscheduler.shared.util.debugger
import dev.ugscheduler.shared.util.deserializer.getFacilitators
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

/**
 * Worker for handling [Facilitator] information retrieval from the remote database to the local database
 */
class FacilitatorWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
    private val dao by lazy { LocalDatabase.get(context).facilitatorDao() }
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    override suspend fun doWork(): Result {
        debugger("De-serializing and adding facilitators")

        // Get all facilitators and deserialize
        val facilitators = getFacilitators(applicationContext)

        // Needed to be called on the background thread
        withContext(IO) {
            // Store locally
            dao.insertAll(facilitators)

            // Forward all course details to remote data source
            for (facilitator in facilitators) {
                try {
                    Tasks.await(
                        firestore.facilitatorDocument(facilitator.id).set(
                            facilitator,
                            SetOptions.merge()
                        )
                    )
                } catch (e: Exception) {
                    debugger("Adding facilitators exception: ${e.localizedMessage}")
                }
            }
        }
        return Result.success()
    }
}