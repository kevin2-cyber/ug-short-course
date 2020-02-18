package shortcourse.readium.core.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

/**
 * Deserializes & loads sample data into the database
 */
class SampleDataWorker(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {

    override suspend fun doWork(): Result {
        // TODO: 2/18/2020 Add sample data to database
        return Result.success()
    }

}