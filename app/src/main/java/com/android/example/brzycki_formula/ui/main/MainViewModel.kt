package com.android.example.brzycki_formula.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.android.example.brzycki_formula.database.ExerciseDatabaseDao

class MainViewModel(val database: ExerciseDatabaseDao,
                    application: Application) : AndroidViewModel(application) {

    var uniqueExercises = database.getAllExercises()

}