/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.shared.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.ugscheduler.shared.data.*
import dev.ugscheduler.shared.repository.AppRepository

/**
 * Factory for view model
 */
class AppViewModelFactory(private val repository: AppRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AppViewModel(repository) as T
    }
}

// ViewModel
open class AppViewModel(private val repository: AppRepository) : ViewModel() {
    fun getAllCourses(context: Context, refresh: Boolean): MutableList<Course> =
        repository.getAllCourses(context, refresh)

    suspend fun getFacilitators(refresh: Boolean): MutableList<Facilitator> =
        repository.getFacilitators(refresh)

    fun getFacilitatorById(id: String, refresh: Boolean): LiveData<Facilitator?> =
        repository.getFacilitatorById(id, refresh)

    fun enrolStudent(enrolment: Enrolment) = repository.enrolStudent(enrolment)

    fun getCurrentStudent(refresh: Boolean): LiveData<Student> =
        repository.getCurrentStudent(refresh)

    fun getCurrentFacilitator(refresh: Boolean): LiveData<Facilitator> =
        repository.getCurrentFacilitator(refresh)

    fun getMyCourses(refresh: Boolean): MutableList<Course> = repository.getMyCourses(refresh)

    fun getCoursesForFacilitator(facilitatorId: String, refresh: Boolean): MutableList<Course> =
        repository.getCoursesForFacilitator(facilitatorId, refresh)

    fun sendFeedback(feedback: Feedback) = repository.sendFeedback(feedback)

    fun getStudentById(studentId: String, refresh: Boolean): LiveData<Student> =
        repository.getStudentById(studentId, refresh)

    fun addStudent(student: Student?) = repository.loginStudent(student)

    fun addFacilitator(facilitator: Facilitator?) = repository.loginFacilitator(facilitator)

    fun logout() = repository.logout()

    fun resetLocalCache() = repository.invalidateLocalCaches()

    suspend fun uploadImage(id: String?, uri: Uri?): LiveData<String?> =
        repository.uploadImage(id, uri)
}