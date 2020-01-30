/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.shared.repository

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.*
import com.google.android.gms.tasks.Tasks
import com.google.firebase.storage.StorageReference
import dev.ugscheduler.shared.data.*
import dev.ugscheduler.shared.datasource.local.LocalDataSource
import dev.ugscheduler.shared.datasource.remote.RemoteDataSource
import dev.ugscheduler.shared.util.prefs.UserSharedPreferences
import dev.ugscheduler.shared.worker.UploadImageWorker
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

interface Repository {
    fun getAllCourses(context: Context, refresh: Boolean): MutableList<Course>
    suspend fun getCourseById(id: String, refresh: Boolean): Course?
    suspend fun getFacilitators(refresh: Boolean): MutableList<Facilitator>
    fun getFacilitatorById(id: String, refresh: Boolean): LiveData<Facilitator?>
    fun enrolStudent(enrolment: Enrolment)
    fun getCurrentStudent(refresh: Boolean): LiveData<Student>
    fun getCurrentFacilitator(refresh: Boolean): LiveData<Facilitator>
    fun getMyCourses(refresh: Boolean): MutableList<Course>
    fun getCoursesForFacilitator(facilitatorId: String, refresh: Boolean): MutableList<Course>
    fun sendFeedback(feedback: Feedback)
    fun getStudentById(studentId: String, refresh: Boolean): LiveData<Student>
    fun loginStudent(student: Student?)
    fun loginFacilitator(facilitator: Facilitator?)
    fun logout()
    fun invalidateLocalCaches()
    suspend fun uploadImage(id: String?, uri: Uri?): LiveData<String?>
    suspend fun getNewsUpdates(refresh: Boolean): MutableList<News>
}

/**
 * Uses both data sources to know where to fetch which data
 */
class AppRepository constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val prefs: UserSharedPreferences,
    private val bucket: StorageReference,
    private val workManager: WorkManager
) : Repository {

    override fun getAllCourses(context: Context, refresh: Boolean): MutableList<Course> {
        return if (refresh) remoteDataSource.getAllCourses(context).apply {
            localDataSource.addCourses(
                this
            )
        }
        else localDataSource.getAllCourses(context)
    }

    override suspend fun getFacilitators(refresh: Boolean): MutableList<Facilitator> {
        return if (refresh) remoteDataSource.getFacilitators().apply {
            localDataSource.addFacilitators(
                this
            )
        }
        else localDataSource.getFacilitators()
    }

    override fun getFacilitatorById(id: String, refresh: Boolean): LiveData<Facilitator?> {
        val liveFacilitator = MutableLiveData<Facilitator>()
        return if (refresh) {
            remoteDataSource.getFacilitatorById(id).observeForever {
                if (it != null) {
                    liveFacilitator.postValue(it)
                    localDataSource.addFacilitator(it)
                }
            }
            liveFacilitator
        } else
            localDataSource.getFacilitatorById(id)
    }

    override fun enrolStudent(enrolment: Enrolment) =
        remoteDataSource.enrolStudent(enrolment).apply { localDataSource.enrolStudent(enrolment) }

    override fun getCurrentStudent(refresh: Boolean): LiveData<Student> {
        val liveStudent = MutableLiveData<Student>()
        return if (refresh) {
            remoteDataSource.getCurrentStudent(prefs.uid).observeForever { student ->
                if (student != null) {
                    localDataSource.addStudent(student)
                }
                liveStudent.postValue(student)
            }
            liveStudent
        } else
            localDataSource.getCurrentStudent(prefs.uid)
    }

    override fun getCurrentFacilitator(refresh: Boolean): LiveData<Facilitator> {
        val liveData = MutableLiveData<Facilitator>()
        if (refresh) remoteDataSource.getCurrentFacilitator(prefs.uid).observeForever { facilitator ->
            if (facilitator != null) {
                localDataSource.addFacilitator(facilitator)
            }
            liveData.postValue(facilitator)
        }
        else {
            localDataSource.getCurrentFacilitator(prefs.uid).observeForever { facilitator ->
                liveData.postValue(facilitator)
            }
        }
        return liveData
    }

    override fun getMyCourses(refresh: Boolean): MutableList<Course> {
        return if (refresh) remoteDataSource.getMyCourses(prefs.uid).apply {
            localDataSource.addMyCourses(
                this
            )
        }
        else localDataSource.getMyCourses(prefs.uid)
    }

    override fun getCoursesForFacilitator(
        facilitatorId: String,
        refresh: Boolean
    ): MutableList<Course> {
        return if (refresh) remoteDataSource.getCoursesForFacilitator(facilitatorId).apply {
            localDataSource.addCourses(
                this
            )
        }
        else localDataSource.getCoursesForFacilitator(facilitatorId)
    }

    override fun sendFeedback(feedback: Feedback) {
        remoteDataSource.sendFeedback(feedback).apply { localDataSource.sendFeedback(feedback) }
    }

    override fun getStudentById(studentId: String, refresh: Boolean): LiveData<Student> {
        return if (refresh) remoteDataSource.getStudentById(studentId).apply {
            localDataSource.addStudent(
                this.value
            )
        }
        else localDataSource.getStudentById(studentId)
    }

    override fun loginStudent(student: Student?) {
        localDataSource.addStudent(student).apply { remoteDataSource.addStudent(student) }
        prefs.login(student?.id)
    }

    override fun loginFacilitator(facilitator: Facilitator?) {
        localDataSource.addFacilitator(facilitator)
            .apply { remoteDataSource.addFacilitator(facilitator) }
        prefs.login(facilitator?.id)
    }

    override fun logout() {
        remoteDataSource.auth.signOut()
        prefs.logout()
    }

    override fun invalidateLocalCaches() = localDataSource.clearDatabase()

    override suspend fun getCourseById(id: String, refresh: Boolean): Course? {
        return if (refresh)
            remoteDataSource.getCourseById(
                id
            ).apply { if (this != null) localDataSource.database.courseDao().insert(this) }
        else localDataSource.getCourseById(
            id
        )
    }

    override suspend fun uploadImage(id: String?, uri: Uri?): LiveData<String?> {
        val liveUri = MutableLiveData<String?>()
        withContext(IO) {
            if (uri != null) {
                try {
                    val downloadUri =
                        Tasks.await(Tasks.await(bucket.putFile(uri)).storage.downloadUrl).toString()
                    liveUri.postValue(downloadUri)
                } catch (_: Exception) {
                    val constraints = Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                    with(workManager) {
                        enqueue(
                            OneTimeWorkRequestBuilder<UploadImageWorker>().setConstraints(
                                constraints
                            ).setInputData(
                                workDataOf(
                                    "uri" to uri.toString(),
                                    "id" to id.toString()
                                )
                            ).build()
                        )
                    }
                }
            }
        }
        return liveUri
    }

    override suspend fun getNewsUpdates(refresh: Boolean): MutableList<News> = withContext(IO){
        if (refresh) remoteDataSource.getNewsArticles().apply { localDataSource.database.newsDao().insertAll(this) }
        else localDataSource.getNewsArticles()
    }
}