/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package io.codelabs.githubrepo.shared.datasource.local

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * Base interface for all Data Access Objects
 */
interface BaseDao<M> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: M)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: MutableList<M>)

    @Delete
    fun delete(item: M)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(item: M)
}