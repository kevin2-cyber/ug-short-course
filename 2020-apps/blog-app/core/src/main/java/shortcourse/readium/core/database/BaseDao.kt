package shortcourse.readium.core.database

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * Base Data Access Object
 */
interface BaseDao<M> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: M)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(item: MutableList<M>)

    @Update
    suspend fun update(item: M)

}