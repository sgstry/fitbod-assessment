package com.android.example.brzycki_formula.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.example.brzycki_formula.R
import com.android.example.brzycki_formula.TextItemViewHolder
import com.android.example.brzycki_formula.database.ExerciseIteration

class ExerciseAdapter : RecyclerView.Adapter<TextItemViewHolder>() {
    var data = listOf<ExerciseIteration>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
        val item = data[position]

        holder.textView.text = item.exerciseName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.exercise_item_view, parent, false) as TextView

        return TextItemViewHolder(view)
    }
}