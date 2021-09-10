package com.android.example.brzycki_formula.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.example.brzycki_formula.database.ExerciseDatabaseDao
import com.android.example.brzycki_formula.database.ExerciseIteration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExerciseHistoryViewModel(val database: ExerciseDatabaseDao,
                               application: Application,
                               exerciseName: String,
                               max: Int
) : AndroidViewModel(application) {
    val _exerciseName = MutableLiveData<String>()
    var exerciseName : LiveData<String>
        get() = _exerciseName
        set(value) {
            _exerciseName.value = value.value
        }

    val _exerciseMax = MutableLiveData<Int>()
    var exerciseMax : LiveData<Int>
        get() = _exerciseMax
        set(value) {
            _exerciseMax.value = value.value
        }

    var exercises: LiveData<List<ExerciseIteration>> = database.getIterationsByName(exerciseName)

    init {
        _exerciseName.value = exerciseName
        _exerciseMax.value = max
        getExerciseIterations()
    }

    private fun getExerciseIterations() {
        viewModelScope.launch {
            if (exerciseName.value != null) {
                getIterations(exerciseName.value!!)
            }
        }
    }

    private suspend fun getIterations(name: String) =
        withContext(Dispatchers.IO) {
            exercises = database.getIterationsByName(name)
        }
}