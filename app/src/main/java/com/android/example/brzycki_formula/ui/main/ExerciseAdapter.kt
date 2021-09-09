package com.android.example.brzycki_formula.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.example.brzycki_formula.R
import com.android.example.brzycki_formula.database.Exercise

class ExerciseAdapter : RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.list_item_exercise, parent, false)

        return ViewHolder(view)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val exerciseName: TextView = itemView.findViewById(R.id.exercise_name)
        val exerciseMax: TextView = itemView.findViewById(R.id.exercise_max)
    }
}