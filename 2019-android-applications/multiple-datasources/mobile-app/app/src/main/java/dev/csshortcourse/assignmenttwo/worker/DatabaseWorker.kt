package dev.csshortcourse.assignmenttwo.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dev.csshortcourse.assignmenttwo.PiedPiper
import dev.csshortcourse.assignmenttwo.datasource.local.LocalDataSource
import dev.csshortcourse.assignmenttwo.model.User

class DatabaseWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
    private val localDataSource by lazy { LocalDataSource(PiedPiper()) }

    override suspend fun doWork(): Result {
        // Create 4 dummy users
        val user1 = User("Samuel Antwi")
        val user2 = User("Isaac Lemar")
        val user3 = User("Eunice Agyei")
        val user4 = User("Leslie Tetteh")

        val userList = mutableListOf<User>()
        userList.add(user1)
        userList.add(user2)
        userList.add(user3)
        userList.add(user4)

        // Store users locally
        localDataSource.userDao.insertAll(userList)
        return Result.success()
    }
}