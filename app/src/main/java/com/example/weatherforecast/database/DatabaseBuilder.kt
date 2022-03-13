package com.example.howsweather.database

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.example.howsweather.model.Forecast

@Database(entities = [Forecast::class], version = 1)
@TypeConverters(Converter::class)
public abstract class DatabaseBuilder : RoomDatabase() {

    companion object {
        @Volatile
        private var databaseBuilder: DatabaseBuilder? = null
        @Synchronized
        public fun getInstance(context: Context): DatabaseBuilder {
            if (databaseBuilder == null) {
                databaseBuilder =
                    Room.databaseBuilder(context, DatabaseBuilder::class.java, "forecast").build()
            }
            return databaseBuilder!!
        }
    }

//    private var databaseBuilder: DatabaseBuilder? = null
//
//    @Synchronized
//     fun getInstance(context: Context): DatabaseBuilder? {
//        if (databaseBuilder == null) {
//            databaseBuilder = Room.databaseBuilder(
//                context.applicationContext,
//                DatabaseBuilder::class.java, "forecast"
//            ).build()
//        }
//        return databaseBuilder
//    }

    override fun createOpenHelper(config: DatabaseConfiguration?): SupportSQLiteOpenHelper {
        TODO("Not yet implemented")
    }

    override fun createInvalidationTracker(): InvalidationTracker {
        TODO("Not yet implemented")
    }

    override fun clearAllTables() {
        TODO("Not yet implemented")
    }

    abstract fun getDao(): Dao?
}