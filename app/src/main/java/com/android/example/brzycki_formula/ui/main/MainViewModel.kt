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
            val uniqueExercises = mutableListOf<String>()
            lineList.forEach {
                val data = it.split(",")
                if (!uniqueExercises.contains(data[1])) {
                    val exercise = Exercise(exerciseName = data[1])
                    insertExercise(exercise)
                    uniqueExercises.add(data[1])
                }

                val exerciseIteration = ExerciseIteration(
                    date = data[0],
                    exerciseName = data[1],
                    sets = data[2].toInt(),
                    reps = data[3].toInt(),
                    weight = data[4].toInt()
                )
                insertIteration(exerciseIteration)
            }
        }

    private suspend fun clear() {
        database.clear()
    }

    private suspend fun insertIteration(newExerciseIteration: ExerciseIteration) {
        database.insertIteration(newExerciseIteration)
    }

    private suspend fun insertExercise(newExercise: Exercise) {
        database.insertExercise(newExercise)
    }
}