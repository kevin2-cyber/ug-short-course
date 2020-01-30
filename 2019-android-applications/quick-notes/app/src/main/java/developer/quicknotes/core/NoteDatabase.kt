package developer.quicknotes.core

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import developer.quicknotes.data.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun dao(): NoteDAO

    /**
     * Create static objects
     */
    companion object {

        private var instance: NoteDatabase? = null

        // Creates an instance of the database
        fun getDatabaseInstance(context: Context): NoteDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(context, NoteDatabase::class.java, "note_db")
                    .build().also { instance = it }
            }
        }

    }
}