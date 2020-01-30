package io.codelabs.ugcloudchat.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import io.codelabs.ugcloudchat.model.Chat
import io.codelabs.ugcloudchat.model.WhatsappUser
import io.codelabs.ugcloudchat.model.worker.LocalContactWorker
import io.codelabs.ugcloudchat.util.UGCloudChatConstants

/**
 * [RoomDatabase] subclass
 * Creates a [Room] database instance on the device
 */
@Database(
    entities = [WhatsappUser::class, Chat::class],
    version = UGCloudChatDB.DATABASE_VERSION,
    exportSchema = true
)
abstract class UGCloudChatDB : RoomDatabase() {

    /**
     * Instance of the DAO interface
     */
    abstract fun dao(): UGCloudChatDao


    companion object {
        const val DATABASE_VERSION = 2

        @Volatile
        private var instance: UGCloudChatDB? = null

        /**
         * Creates an instance of the Room database class
         */
        fun getInstance(context: Context): UGCloudChatDB = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context,
                UGCloudChatDB::class.java,
                UGCloudChatConstants.DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

                        // Worker loads all contacts stored on the user's device
                        // and compare it to the ones online when the user launches the app after the first
                        // installation

                        // Create a constraint for our worker to run
                        val constraints = Constraints.Builder()
                            .setRequiresBatteryNotLow(true)
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build()

                        // Create a one-time worker to load all contacts
                        val contactWorkRequest = OneTimeWorkRequestBuilder<LocalContactWorker>()
                            .setConstraints(constraints)
                            .build()

                        // Start the worker once the database is created for the first time
                        WorkManager.getInstance(context).enqueue(contactWorkRequest)

                    }
                }).build().also { instance = it }
        }
    }
}