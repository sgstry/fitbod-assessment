package com.android.example.brzycki_formula.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_table")
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    var exerciseId: Long = 0L,

    @ColumnInfo(name = "exercise_name")
    var exerciseName: String = "",

    @ColumnInfo(name = "exercise_max")
    var max: Int = 0

)
