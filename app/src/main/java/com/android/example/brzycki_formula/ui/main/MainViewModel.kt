package com.android.example.brzycki_formula.ui.main

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.android.example.brzycki_formula.R
import com.android.example.brzycki_formula.Util
import com.android.example.brzycki_formula.database.Exercise
import com.android.example.brzycki_formula.database.ExerciseDatabaseDao
import com.android.example.brzycki_formula.database.ExerciseIteration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import kotlin.math.roundToInt

class MainViewModel(val database: ExerciseDatabaseDao,
                    application: Application) : AndroidViewModel(application) {

    private val app = application
    var exercises : LiveData<List<Exercise>> = database.getExercises()
    private val hashPref = app.getString(R.string.data_hash)
    val prefs: SharedPreferences = getDefaultSharedPreferences(app)

    init {
        instantiateDb()
    }

    private fun instantiateDb() {
        val hash = prefs.getString(hashPref, "")
        val fileAsString = Util.getFileAsString(app)
        var newHash = Util.getFileChecksum(fileAsString)
        var readInToDb = false
        if (hash == "" || hash != newHash) {
            readInToDb = true
            val editor = prefs.edit()
            editor.putString(hashPref, newHash)
            editor.apply()
        }

        val lines = fileAsString.lines()
        viewModelScope.launch {
            if (readInToDb) {
                clear()
                readInExerciseData(lines)
            }
            exercises = database.getExercises()
        }
    }

    private suspend fun readInExerciseData(input: List<String>) =
        withContext(Dispatchers.IO) {
            val dateFormat = SimpleDateFormat("MMM dd yyyy")
            input.forEach {
                val data = it.split(",")
                val exerciseName = data[1]
                val reps: Int = data[3].toInt()
                val weight : Int = data[4].toInt()
                val oneRepMax = calculateOneRepMax(weight, reps)

                val exerciseIteration = ExerciseIteration(
                    date = dateFormat.parse(data[0]),
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
        return (weight.toDouble() * (36.0 / (37.0 - reps))).roundToInt()
    }

    private suspend fun clear() {
        database.clearIterations()
    }

    private suspend fun insertIteration(newExerciseIteration: ExerciseIteration) {
        database.insertIteration(newExerciseIteration)
    }
}