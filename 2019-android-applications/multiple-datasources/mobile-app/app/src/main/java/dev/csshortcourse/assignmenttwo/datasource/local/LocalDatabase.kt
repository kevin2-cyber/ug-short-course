package dev.csshortcourse.assignmenttwo.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.csshortcourse.assignmenttwo.model.Chat
import dev.csshortcourse.assignmenttwo.model.User
import dev.csshortcourse.assignmenttwo.util.Utils


@Database(
    entities = [User::class, Chat::class],
    version = Utils.DATABASE_VERSION,
    exportSchema = true
)
abstract class LocalDatabase : RoomDatabase() {
    // Configure User DAO
    abstract fun userDao(): UserDao

    // Configure Chat DAO
    abstract fun chatDao(): ChatDao

    companion object {
        // Create singletons for our database
        @Volatile
        private var instance: LocalDatabase? = null

        // Creates an instance of our abstract
        fun get(context: Context): LocalDatabase = instance ?: synchronized(this) {
            instance ?: Room
                .databaseBuilder(context, LocalDatabase::class.java, Utils.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build().also { instance = it }
        }

    }
}