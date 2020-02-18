package shortcourse.readium.core.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * Base Data Access Object
 */
interface BaseDao<M> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: M)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(item: MutableList<M>)

    @Delete
    fun delete(item: M)

    @Update
    fun update(item: M)

}