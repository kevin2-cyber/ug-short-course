package io.codelabs.churchinc.core.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.codelabs.churchinc.BuildConfig
import io.codelabs.churchinc.model.User

@Database(entities = [User::class], version = BuildConfig.VERSION_CODE, exportSchema = true)
abstract class ChurchIncDatabase : RoomDatabase() {

    // CREATES A DAO FUNCTIONS FOR US
    abstract fun dao(): ChurchIncDao

    companion object {

        @Volatile
        private var instance: ChurchIncDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): ChurchIncDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context,
                    ChurchIncDatabase::class.java,
                    "church_inc_db"
                ).fallbackToDestructiveMigration()
                    .build().also { instance = it }
            }
        }

    }

}