package com.android.example.brzycki_formula.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.android.example.brzycki_formula.R
import com.android.example.brzycki_formula.database.Exercise
import com.android.example.brzycki_formula.database.ExerciseDatabaseDao
import com.android.example.brzycki_formula.database.ExerciseIteration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream

class MainViewModel(val database: ExerciseDatabaseDao,
                    application: Application) : AndroidViewModel(application) {

    private val app = application
    var exercises : LiveData<List<Exercise>>

    init {
        instantiateDb()
        exercises = database.getAllExercises()
    }

    private fun instantiateDb() {
        viewModelScope.launch {
            clear()
            readInExerciseData()
        }
    }

    private suspend fun readInExerciseData() =
        withContext(Dispatchers.IO) {
            val inputStream: InputStream = app.resources.openRawResource(R.raw.workoutdata)
            val lineList = mutableListOf<String>()

            inputStream.bufferedReader().useLines { lines -> lines.forEach { lineList.add(it)} }
            val exerciseMap = mutableMapOf<String,Int>()
            lineList.forEach {
                val data = it.split(",")
                val exerciseName = data[1]
                val reps: Int = data[3].toInt()
                val weight : Int = data[4].toInt()
                val oneRepMax = calculateOneRepMax(weight, reps)
                if (exerciseMap.contains(exerciseName) && oneRepMax > exerciseMap[exerciseName]!!) {
                    exerciseMap[exerciseName] = oneRepMax
                } else if(!exerciseMap.contains(exerciseName)){
                    exerciseMap[exerciseName] = oneRepMax
                }

                val exerciseIteration = ExerciseIteration(
                    date = data[0],
                    exerciseName = exerciseName,
                    sets = data[2].toInt(),
                    reps = data[3].toInt(),
                    weight = weight
                )
                insertIteration(exerciseIteration)
            }
            exerciseMap.forEach {
                val exercise = Exercise(exerciseName = it.key, max = it.value)
                insertExercise(exercise)
            }
        }

    private fun calculateOneRepMax(weight: Int, reps: Int): Int {
        return weight / ((37 / 36) - ((1 / 36) * reps))
    }

    private suspend fun clear() {
        database.clearIterations()
        database.clearExercises()
    }

    private suspend fun insertIteration(newExerciseIteration: ExerciseIteration) {
        database.insertIteration(newExerciseIteration)
    }

    private suspend fun insertExercise(newExercise: Exercise) {
        database.insertExercise(newExercise)
    }
}