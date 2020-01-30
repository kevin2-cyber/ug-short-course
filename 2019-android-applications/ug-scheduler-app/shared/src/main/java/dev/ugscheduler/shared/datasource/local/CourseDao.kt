/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.shared.datasource.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import dev.ugscheduler.shared.data.Course

@Dao
interface CourseDao : BaseDao<Course> {
    @Query("select * from courses where id = :id")
    suspend fun getCourseById(id: String?): Course?

    @Query("select * from courses where id = :id")
    fun getCourseDetails(id: String): LiveData<Course>

    @Query("select * from courses order by name asc")
    fun getAllCourses(): LiveData<MutableList<Course>>

    @Query("select * from courses where facilitator = :facilitator order by name asc")
    fun getCoursesForFacilitator(facilitator: String): MutableList<Course>
}