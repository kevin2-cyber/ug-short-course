package shortcourse.readium.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import shortcourse.readium.core.model.account.Account
import shortcourse.readium.core.model.post.Comment
import shortcourse.readium.core.model.post.Post
import shortcourse.readium.core.util.DatabaseUtil

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
            )
                .fallbackToDestructiveMigrationOnDowngrade()
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // TODO: 2/18/2020 When database is created, preload with data from remote database
                    }
                })
                .allowMainThreadQueries()
                .build().also { instance = it }
        }
    }

}