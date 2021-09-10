package com.android.example.brzycki_formula.database

import androidx.room.ColumnInfo

data class Exercise(
        @ColumnInfo(name = "exercise_name") val exerciseName: String,
        @ColumnInfo(name = "max") val max: Int
)
