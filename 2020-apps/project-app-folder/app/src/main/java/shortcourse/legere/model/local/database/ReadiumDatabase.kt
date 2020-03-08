package shortcourse.legere.model.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import shortcourse.legere.model.entities.Post
import shortcourse.legere.model.entities.User
import shortcourse.legere.model.local.dao.PostDao
import shortcourse.legere.model.local.dao.UserDao


/**
 * This is the application's local database class
 * Entities -> [User] & [Post]
 * DAOs -> [UserDao] & [PostDao]
 */
@TypeConverters(ListTypeConverter::class)
@Database(entities = [User::class, Post::class], version = 1)
abstract class ReadiumDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun postDao(): PostDao

}

class ListTypeConverter {

    @TypeConverter
    fun listToJson(value: List<String>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<String>? {
        val objects = Gson().fromJson(value, Array<String>::class.java) as Array<String>
        return objects.toList()
    }
}