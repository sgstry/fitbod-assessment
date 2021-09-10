package com.android.example.brzycki_formula.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_iteration_table")
data class ExerciseIteration(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "exercise_date")
    var date: String = "",

    @ColumnInfo(name = "exercise_name")
    var exerciseName: String = "",

    @ColumnInfo(name = "sets")
    var sets: Int = 0,

    @ColumnInfo(name = "reps")
    var reps: Int = 0,

    @ColumnInfo(name = "weight")
    var weight: Int = 0,

    @ColumnInfo(name = "max")
    var max: Int = 0
)