package developer.quicknotes.core

import androidx.lifecycle.LiveData
import androidx.room.*
import developer.quicknotes.data.Note

/**
 * Data Access Object
 *
 * This annotation marks a class or interface as a Data Access Object.
 * Data access objects are the main component of Room
 * that are responsible for defining the methods that access the database.
 * The class that is annotated with Database must have an abstract
 * method that has 0 arguments and returns the class that is annotated with Dao.
 * While generating the code at compile time, Room will generate an implementation of this class.
 */
@Dao
interface NoteDAO {

    /**
     * Create note
     * Read note(s)
     * Update note
     * Delete note
     */

    @Insert
    fun createNote(note: Note)

    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    fun readNotes(): LiveData<MutableList<Note>>

    @Query("SELECT * FROM notes WHERE id = :id")
    fun readSingleNote(id: Int): LiveData<Note>

    @Update
    fun updateNote(note: Note?   )

    @Delete
    fun deleteNote(note: Note?)

}