package dev.csshortcourse.assignmenttwo

import android.app.Application
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dev.csshortcourse.assignmenttwo.worker.DatabaseWorker

class PiedPiper : Application() {

    override fun onCreate() {
        super.onCreate()

        // AppPreferences.get(applicationContext).logout()

        // Start worker to add data to database
        with(WorkManager.getInstance(applicationContext)) {
            val builder = OneTimeWorkRequestBuilder<DatabaseWorker>()
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
            enqueue(mutableListOf(builder.build()))
        }
    }
}