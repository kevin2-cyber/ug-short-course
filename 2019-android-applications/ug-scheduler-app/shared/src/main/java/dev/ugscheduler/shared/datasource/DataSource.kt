/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.shared.datasource

import android.content.Context
import androidx.lifecycle.LiveData
import dev.ugscheduler.shared.data.*
import dev.ugscheduler.shared.util.deserializer.getCourses

/**
 * Base data source
 */
interface DataSource {
    fun getAllCourses(context: Context): MutableList<Course> = getCourses(context)
    suspend fun getCourseById(id:String?): Course?
    suspend fun getFacilitators(): MutableList<Facilitator>
    fun getFacilitatorById(id: String): LiveData<Facilitator?>
    fun enrolStudent(enrolment: Enrolment)
    fun getCurrentStudent(id: String): LiveData<Student>
    fun getCurrentFacilitator(id: String): LiveData<Facilitator?>
    fun getMyCourses(studentId: String): MutableList<Course>
    fun getCoursesForFacilitator(facilitatorId: String): MutableList<Course>
    fun sendFeedback(feedback: Feedback)
    fun getStudentById(studentId: String): LiveData<Student>

    suspend fun getNewsArticles(): MutableList<News>
}