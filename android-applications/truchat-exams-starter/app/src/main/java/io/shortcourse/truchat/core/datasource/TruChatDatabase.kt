package io.shortcourse.truchat.core.datasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.shortcourse.truchat.model.Message
import io.shortcourse.truchat.model.User
import io.shortcourse.truchat.util.Constants

@Database(entities = [User::class, Message::class, io.shortcourse.truchat.model.Room::class], version = 2, exportSchema = true)
abstract class TruChatDatabase : RoomDatabase() {

    /**
     * Data access object: Entry point for the [Room] DAO
     */
    abstract fun dao(): TruChatDao

    companion object {
        @Volatile
        private var instance: TruChatDatabase? = null

        /**
         * Entry point for the [TruChatDatabase]
         */
        fun get(context: Context): TruChatDatabase {
            return instance ?: synchronized(this) {
                instance
                        ?: Room.databaseBuilder(context, TruChatDatabase::class.java, Constants.DB_REF)
                                .fallbackToDestructiveMigration()
                                .build().also { instance = it }
            }
        }
    }

}