package com.gabby.model.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao <T> {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insert (item: T)

    @Update (onConflict = OnConflictStrategy.REPLACE)
    fun update(item: T)

    @Delete
    fun delete (item: T)
}