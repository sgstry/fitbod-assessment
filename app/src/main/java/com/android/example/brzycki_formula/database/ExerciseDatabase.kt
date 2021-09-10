package com.android.example.brzycki_formula.database

import android.content.Context
import androidx.room.*
import java.util.*

@Database(entities = [ExerciseIteration::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ExerciseDatabase : RoomDatabase() {
    abstract val exerciseDatabaseDao: ExerciseDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: ExerciseDatabase? = null
        fun getInstance(context: Context) : ExerciseDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext,
                        ExerciseDatabase::class.java,
                        "exercise_history_database").fallbackToDestructiveMigration().build()
                    INSTANCE = instance

                }
                return instance
            }

        }
    }
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}