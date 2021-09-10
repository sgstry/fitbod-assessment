package com.android.example.brzycki_formula.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.example.brzycki_formula.ExerciseHistoryActivity
import com.android.example.brzycki_formula.database.Exercise
import com.android.example.brzycki_formula.databinding.ListItemExerciseBinding

class ExerciseAdapter() : RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {
    var data = listOf<Exercise>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }



    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.exerciseName.text = item.exerciseName
        holder.exerciseMax.text = item.max.toString()

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context.applicationContext
            val historyActivityIntent = Intent(
                context,
                ExerciseHistoryActivity::class.java).apply {
                    putExtra(ExerciseHistoryActivity.EXTRA_EXERCISE_NAME, item.exerciseName)
                    putExtra(ExerciseHistoryActivity.EXTRA_EXERCISE_MAX, item.max)
                }
            context.startActivity(historyActivityIntent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val binding =
            ListItemExerciseBinding.inflate(layoutInflater, parent, false)

        return ViewHolder(binding)
    }

    class ViewHolder(val binding:ListItemExerciseBinding) : RecyclerView.ViewHolder(binding.root) {
        val exerciseName: TextView = binding.exerciseName
        val exerciseMax: TextView = binding.exerciseMax
    }
}