package io.hackernews.shared.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * Base Data Access Object for all DAOs
 */
interface BaseDao<Model> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(model: Model)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(models: MutableList<Model>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(model: Model)

    @Delete
    fun delete(model: Model)

}