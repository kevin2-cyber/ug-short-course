package shortcourse.readium.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import shortcourse.readium.core.model.account.Account
import shortcourse.readium.core.model.post.Comment
import shortcourse.readium.core.model.post.Post
import shortcourse.readium.core.util.DatabaseUtil
import shortcourse.readium.core.util.Entities
import shortcourse.readium.core.worker.SampleDataWorker

@Database(
        entities = [Post::class, Account::class, Comment::class],
        version = DatabaseUtil.VERSION,
        exportSchema = false
)
@TypeConverters(ListTypeConverter::class)
abstract class ReadiumDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao
    abstract fun accountDao(): AccountDao
    abstract fun commentDao(): CommentDao


    companion object {
        @Volatile
        private var instance: ReadiumDatabase? = null

        fun getInstance(context: Context): ReadiumDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                    context,
                    ReadiumDatabase::class.java,
                    DatabaseUtil.NAME
            ).addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigrationOnDowngrade()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            with(WorkManager.getInstance(context)) {
                                enqueue(OneTimeWorkRequestBuilder<SampleDataWorker>().build())
                            }
                        }
                    })
                    .allowMainThreadQueries()
                    .build().also { instance = it }
        }

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("alter table ${Entities.ACCOUNTS} add column username TEXT")
            }
        }
    }

}