package com.android.example.brzycki_formula.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExerciseDatabaseDao {

    @Insert
    suspend fun insertIteration(night: ExerciseIteration)

    @Query("DELETE FROM exercise_iteration_table")
    suspend fun clearIterations()

    @Query("SELECT * FROM exercise_iteration_table WHERE exercise_name = :name ORDER BY exercise_date ASC")
    fun getIterationsByName(name: String): LiveData<List<ExerciseIteration>>

    @Query("SELECT exercise_name, max(max) as max FROM exercise_iteration_table GROUP BY exercise_name")
    fun getExercises(): LiveData<List<Exercise>>

}