package shortcourse.legere.model.local.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

// Create
// Read
// Update
// Delete
interface ReadiumDao<T> {

    @Insert
    fun insert(item: T)

    @Insert
    fun insertAll(item: MutableList<T>)

    @Update
    fun update(item: T)

    @Delete
    fun delete(item: T)
}