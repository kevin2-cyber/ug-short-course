package shortcourse.readium.core.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import shortcourse.readium.core.util.Entities
import shortcourse.readium.core.util.debugger

/**
 * Workaround for list conversion
 */
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
    fun numListToJson(value: List<Int>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToNumList(value: String): List<Int>? {
        val objects = Gson().fromJson(value, Array<Int>::class.java) as Array<Int>
        return objects.toList()
    }

    @TypeConverter
    fun listToRoleJson(value: List<Entities.Role>?): String? {
        return Gson().toJson(value?.map { it.label }) ?: null
    }

    @TypeConverter
    fun jsonToRoleList(value: String?): List<Entities.Role>? {
        val objects =
            Gson().fromJson(value, Array<String>::class.java) as Array<String>?
        return objects?.map { Entities.Role.valueOf(it) }?.toList() ?: emptyList()
    }
}