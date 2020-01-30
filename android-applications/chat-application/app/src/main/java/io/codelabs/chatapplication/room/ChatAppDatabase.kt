package io.codelabs.chatapplication.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.codelabs.chatapplication.data.User

/**
 * [RoomDatabase] implementation
 */
@Database(entities = [User::class], version = 2, exportSchema = true)
abstract class ChatAppDatabase : RoomDatabase() {

    companion object {
        private val lock = Any()
        @Volatile
        private var instance: ChatAppDatabase? = null

        fun getInstance(context: Context): ChatAppDatabase = instance ?: synchronized(lock) {
            instance ?: Room.databaseBuilder(context, ChatAppDatabase::class.java, "chatapp.db")
                .fallbackToDestructiveMigration()
                .build().also { instance = it }
        }
    }

    /**
     * Get an instance of the Data Access Object
     */
    abstract fun dao(): ChatAppDao

}