/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.shared.datasource.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import dev.ugscheduler.shared.data.Facilitator

@Dao
interface FacilitatorDao : BaseDao<Facilitator> {
    @Query("select * from facilitators order by fullName asc")
    fun getAllFacilitators(): MutableList<Facilitator>

    @Query("select * from facilitators where id = :id")
    fun getFacilitatorById(id: String): LiveData<Facilitator?>

    @Query("select * from facilitators where id = :id")
    fun getFacilitatorByIdAsync(id: String): Facilitator?
}