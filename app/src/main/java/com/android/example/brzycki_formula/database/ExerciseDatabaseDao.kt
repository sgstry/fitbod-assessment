package com.android.example.brzycki_formula.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ExerciseDatabaseDao {

    @Insert
    suspend fun insertIteration(night: ExerciseIteration)

    @Update
    suspend fun updateIteration(night: ExerciseIteration)

    @Query("SELECT * from exercise_iteration_table WHERE id = :key")
    suspend fun getIteration(key: Long) : ExerciseIteration?

    @Query("DELETE FROM exercise_iteration_table")
    suspend fun clear()

    @Query("SELECT * FROM exercise_iteration_table ORDER BY id DESC")
    fun getAllIterations(): List<ExerciseIteration>

    @Query("SELECT * FROM exercise_table ORDER BY exerciseId DESC")
    fun getAllExercises(): LiveData<List<Exercise>>


}