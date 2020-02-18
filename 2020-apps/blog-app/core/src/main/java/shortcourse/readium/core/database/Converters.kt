package shortcourse.readium.core.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import shortcourse.readium.core.util.Entities

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

    @TypeConverter
    fun listToRoleJson(value: List<Entities.Role>?): String {
        return Gson().toJson(value?.map { it.label })
    }

    @TypeConverter
    fun jsonToRoleList(value: String): List<Entities.Role>? {
        val objects =
            Gson().fromJson(value, Array<String>::class.java) as Array<String>
        return objects.map { Entities.Role.valueOf(it) }.toList()
    }
}