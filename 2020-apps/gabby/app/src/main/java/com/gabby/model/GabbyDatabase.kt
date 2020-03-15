package com.gabby.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gabby.model.entities.Post
import com.gabby.model.entities.User

@Database(entities=[User::class, Post::class], version= 1)
abstract class GabbyDatabase : RoomDatabase() {
    abstract fun userDao() 
    abstract fun postDao()
}