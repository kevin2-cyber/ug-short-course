package dev.csshortcourse.assignmenttwo.datasource.local

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

/**
 * Base Data Access Object
 */
interface BaseDao<O> {
    @Insert
    suspend fun insert(item: O?)

    @Update
    suspend fun update(item: O)

    @Insert
    suspend fun insertAll(item: MutableList<O>)

    @Delete
    suspend fun remove(item: O?)
}