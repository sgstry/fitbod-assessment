package com.android.example.brzycki_formula

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.example.brzycki_formula.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        title = application.getString(R.string.mainLabel)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}