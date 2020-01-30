package io.hackernews.shared.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import io.hackernews.shared.data.Story

/**
 * DAO for stories
 */
@Dao
interface StoryDao : BaseDao<Story> {

    @Query("select * from stories order by id desc")
    fun getAllStories(): LiveData<List<Story>>

    @Query("select * from stories order by id desc")
    fun getStories(): List<Story>

}