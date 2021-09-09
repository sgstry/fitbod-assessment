package com.android.example.brzycki_formula.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ExerciseIteration::class], version = 1, exportSchema = false)
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