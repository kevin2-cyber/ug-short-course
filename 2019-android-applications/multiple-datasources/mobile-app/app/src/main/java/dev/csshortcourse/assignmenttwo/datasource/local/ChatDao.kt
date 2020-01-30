package dev.csshortcourse.assignmenttwo.datasource.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import dev.csshortcourse.assignmenttwo.model.Chat

/**
 * [Chat] Data Access Object
 */
@Dao
interface ChatDao : BaseDao<Chat> {
    @Query("select * from chats where sender = :sender and recipient = :recipient order by timestamp asc")
    fun getMyChats(sender: String?, recipient: String): LiveData<MutableList<Chat>>
}