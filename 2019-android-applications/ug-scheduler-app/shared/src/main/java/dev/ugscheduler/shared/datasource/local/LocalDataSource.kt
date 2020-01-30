/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.shared.datasource.local

import androidx.lifecycle.LiveData
import dev.ugscheduler.shared.data.*
import dev.ugscheduler.shared.datasource.DataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Local data source
 */
class LocalDataSource constructor(
    val database: LocalDatabase
) : DataSource {
    private val courseDao: CourseDao by lazy { database.courseDao() }
    private val studentDao: StudentDao by lazy { database.studentDao() }
    private val feedbackDao: FeedbackDao by lazy { database.feedbackDao() }
    private val facilitatorDao: FacilitatorDao by lazy { database.facilitatorDao() }
    private val newsDao: NewsDao by lazy { database.newsDao() }
    private val ioScope = CoroutineScope(IO)

    override fun sendFeedback(feedback: Feedback) = feedbackDao.insert(feedback)

    override fun getStudentById(studentId: String): LiveData<Student> =
        studentDao.getStudent(studentId)

    override suspend fun getFacilitators(): MutableList<Facilitator> = withContext(IO) {
        facilitatorDao.getAllFacilitators()
    }

    override fun getFacilitatorById(id: String): LiveData<Facilitator?> =
        facilitatorDao.getFacilitatorById(id)

    // todo: enable offline enrolment
    override fun enrolStudent(enrolment: Enrolment) {}

    override fun getCurrentStudent(id: String): LiveData<Student> = studentDao.getStudent(id)

    override fun getCurrentFacilitator(id: String): LiveData<Facilitator?> =
        facilitatorDao.getFacilitatorById(id)

    // todo: get my courses
    override fun getMyCourses(studentId: String): MutableList<Course> = mutableListOf()

    override fun getCoursesForFacilitator(facilitatorId: String): MutableList<Course> {
        val courses = mutableListOf<Course>()
        ioScope.launch {
            courses.addAll(courseDao.getCoursesForFacilitator(facilitatorId))
        }
        return courses
    }

    fun addFacilitators(facilitators: MutableList<Facilitator>) {
        ioScope.launch {
            facilitatorDao.insertAll(facilitators)
        }
    }

    fun addCourses(courses: MutableList<Course>) {
        ioScope.launch {
            courseDao.insertAll(courses)
        }
    }

    fun addFacilitator(facilitator: Facilitator?) {
        ioScope.launch {
            if (facilitator != null) facilitatorDao.insert(facilitator)
        }
    }

    fun addStudent(student: Student?) {
        ioScope.launch {
            if (student != null) studentDao.insert(student)
        }
    }

    fun addMyCourses(courses: MutableList<Course>) {
        // todo: Add my courses
    }

    // Clear local database
    fun clearDatabase() = database.clearAllTables()

    override suspend fun getCourseById(id: String?): Course? = courseDao.getCourseById(id)

    override suspend fun getNewsArticles(): MutableList<News> = newsDao.getNewsArticlesAsync()
}