package io.hackernews.shared.database

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import io.hackernews.shared.Constants
import io.hackernews.shared.data.Story
import io.hackernews.shared.util.toMutableListOfInt
import io.hackernews.shared.worker.StoriesWorker

@TypeConverters(ListTypeConverter::class)
@Database(entities = [Story::class], version = Constants.DB_VER, exportSchema = true)
abstract class HackerNewsDatabase : RoomDatabase() {

    abstract fun storyDao(): StoryDao

    companion object {
        @Volatile
        private var instance: HackerNewsDatabase? = null

        fun getInstance(context: Context): HackerNewsDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context,
                HackerNewsDatabase::class.java,
                Constants.DB_NAME
            )
                .fallbackToDestructiveMigration()
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        WorkManager.getInstance(context).enqueue(
                            mutableListOf(
                                OneTimeWorkRequestBuilder<StoriesWorker>()
                                    .setConstraints(
                                        Constraints.Builder()
                                            .setRequiredNetworkType(NetworkType.CONNECTED)
                                            .build()
                                    )
                                    .build()
                            )
                        )
                    }
                })
                .build()
        }
    }
}

class ListTypeConverter {

    @TypeConverter
    fun toList(string: String?): MutableList<Int> =
        string?.toMutableListOfInt() ?: mutableListOf<Int>()

    @TypeConverter
    fun fromList(list: MutableList<Int>): String = list.toString()
}