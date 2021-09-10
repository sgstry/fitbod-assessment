package com.android.example.brzycki_formula.ui.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.example.brzycki_formula.database.ExerciseDatabaseDao

class ExerciseHistoryViewModelFactory(
    private val dataSource: ExerciseDatabaseDao,
    private val application: Application,
    private val exerciseName: String,
    private val max: Int
)  : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExerciseHistoryViewModel::class.java)) {
            return ExerciseHistoryViewModel(dataSource, application, exerciseName, max) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}