/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.shared.datasource.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import dev.ugscheduler.shared.data.Student

@Dao
interface StudentDao : BaseDao<Student> {

    /**
     * Get a single student
     */
    @Query("select * from students where id = :id")
    fun getStudent(id: String): LiveData<Student>

    /**
     * Get a single student
     */
    @Query("select * from students where id = :id")
    fun getStudentById(id: String): Student
}