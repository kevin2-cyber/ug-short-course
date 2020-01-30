package io.codelabs.chatapplication.room.repository

import io.codelabs.chatapplication.core.ChatApplication
import io.codelabs.chatapplication.data.User
import io.codelabs.chatapplication.room.ChatAppDao
import io.codelabs.chatapplication.room.ChatAppDatabase

/**
 * Data source for logged in [User]
 */
class UserRepository constructor(application: ChatApplication) {

    private val dao: ChatAppDao by lazy { ChatAppDatabase.getInstance(application.applicationContext).dao() }

    fun insert(user: User) = dao.createUser(user)

    fun delete(user: User) = dao.deleteUser(user)

    fun update(user: User) = dao.updateUser(user)

    fun deleteAll(vararg user: User) = dao.deleteAllUser(*user)

    fun getCurrentUser(id: String) = dao.getCurrentUser(id)

    fun getAllUsers() = dao.getAllUsers()

}