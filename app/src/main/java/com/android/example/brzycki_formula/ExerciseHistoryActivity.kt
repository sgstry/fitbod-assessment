package com.android.example.brzycki_formula

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.android.example.brzycki_formula.ui.main.ExerciseHistoryFragment

class ExerciseHistoryActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_EXERCISE_NAME: String = "exerciseName"
        const val EXTRA_EXERCISE_MAX: String = "max"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val exerciseName = intent.getStringExtra(EXTRA_EXERCISE_NAME) ?: ""
        title = exerciseName

        val max = intent.getIntExtra(EXTRA_EXERCISE_MAX, 0)

        setContentView(R.layout.exercise_history_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ExerciseHistoryFragment.newInstance(exerciseName, max))
                .commitNow()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
        }

        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }
}