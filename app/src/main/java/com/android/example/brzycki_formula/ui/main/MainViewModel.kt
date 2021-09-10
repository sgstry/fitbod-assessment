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
    var exercises : LiveData<List<Exercise>> = database.getExercises()

    init {
        instantiateDb()
    }

    private fun instantiateDb() {
        viewModelScope.launch {
            clear()
            readInExerciseData()
            exercises = database.getExercises()
        }
    }

    private suspend fun readInExerciseData() =
        withContext(Dispatchers.IO) {
            val inputStream: InputStream = app.resources.openRawResource(R.raw.workoutdata)
            val lineList = mutableListOf<String>()

            inputStream.bufferedReader().useLines { lines -> lines.forEach { lineList.add(it)} }
            lineList.forEach {
                val data = it.split(",")
                val exerciseName = data[1]
                val reps: Int = data[3].toInt()
                val weight : Int = data[4].toInt()
                val oneRepMax = calculateOneRepMax(weight, reps)

                val exerciseIteration = ExerciseIteration(
                    date = data[0],
                    exerciseName = exerciseName,
                    sets = data[2].toInt(),
                    reps = data[3].toInt(),
                    weight = weight,
                    max = oneRepMax
                )
                insertIteration(exerciseIteration)
            }
        }

    private fun calculateOneRepMax(weight: Int, reps: Int): Int {
        return weight / ((37 / 36) - ((1 / 36) * reps))
    }

    private suspend fun clear() {
        database.clearIterations()
    }

    private suspend fun insertIteration(newExerciseIteration: ExerciseIteration) {
        database.insertIteration(newExerciseIteration)
    }
}