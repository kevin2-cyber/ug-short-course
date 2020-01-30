package io.codelabs.todoapplication.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.codelabs.todoapplication.data.TodoItem

/**
 * [RoomDatabase] implementation
 */
@Database(entities = [TodoItem::class], version = 1, exportSchema = false)
abstract class TodoAppDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var instance: TodoAppDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): TodoAppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context,
                    TodoAppDatabase::class.java,
                    "todoapp.db"
                )
                    .fallbackToDestructiveMigration()
                    .build().also { instance = it }
            }
        }
    }

    /**
     * Get an instance of the Data Access Object
     */
    abstract fun dao(): TodoAppDao

}