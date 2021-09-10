package com.android.example.brzycki_formula.ui.main

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.android.example.brzycki_formula.ExerciseHistoryActivity
import com.android.example.brzycki_formula.R
import com.android.example.brzycki_formula.database.ExerciseDatabase
import com.android.example.brzycki_formula.database.ExerciseIteration
import com.android.example.brzycki_formula.databinding.ExerciseHistoryFragmentBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.components.AxisBase

import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*


class ExerciseHistoryFragment : Fragment() {

    companion object {
        fun newInstance(exerciseName: String, max: Int) : ExerciseHistoryFragment {
            val frag = ExerciseHistoryFragment()
            val args = Bundle()
            args.putString(ExerciseHistoryActivity.EXTRA_EXERCISE_NAME, exerciseName)
            args.putInt(ExerciseHistoryActivity.EXTRA_EXERCISE_MAX, max)
            frag.arguments = args

            return frag
        }
    }

    private lateinit var viewModel: ExerciseHistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: ExerciseHistoryFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.exercise_history_fragment, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = ExerciseDatabase.getInstance(application).exerciseDatabaseDao
        val viewModelFactory = ExerciseHistoryViewModelFactory(
            dataSource,
            application,
            arguments?.getString(ExerciseHistoryActivity.EXTRA_EXERCISE_NAME) ?: "",
            arguments?.getInt(ExerciseHistoryActivity.EXTRA_EXERCISE_MAX) ?: 0)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(ExerciseHistoryViewModel::class.java)

        viewModel.exerciseName.observe(viewLifecycleOwner, Observer {
            binding.exerciseName.text = it
        })

        viewModel.exerciseMax.observe(viewLifecycleOwner, Observer {
            binding.exerciseMax.text = it.toString()
        })

        viewModel.exercises.observe(viewLifecycleOwner, Observer { exercises ->
            if (container != null) {
                buildChart(exercises, binding.chart, container.context)
            }
        })

        binding.lifecycleOwner = this
        binding.historyViewModel = viewModel

        return binding.root
    }

    private fun buildChart(exercises: List<ExerciseIteration>, chart: LineChart, context: Context) {
        val oneRepMaxProgressionList = mutableListOf<ExerciseIteration>()
        var currentMax = 0
        exercises.forEach {
            val max = it.max
            if (max > currentMax) {
                oneRepMaxProgressionList.add(it)
                currentMax = max
            }
        }
        val totalExercisesProgressed = oneRepMaxProgressionList.size
        val values = mutableListOf<Entry>()
        for (i in 0 until totalExercisesProgressed) {
            val exercise: ExerciseIteration = oneRepMaxProgressionList[i]
            val entry = Entry()
            entry.x = exercise.date?.time?.toFloat() ?: 0f
            entry.y = exercise.max.toFloat()
            values.add(entry)
        }

        val redColor = getColor(context, R.color.statusBar_red)
        val whiteColor = getColor(context, R.color.white)
        val dataSet = LineDataSet(values, "1 Rep Max Record Progression")
        dataSet.valueTextColor = whiteColor
        dataSet.valueTextSize = 8f
        dataSet.setDrawCircles(true)
        dataSet.enableDashedLine(10f, 0f, 0f)
        dataSet.color = redColor
        dataSet.setDrawValues(true)

        val dataSets = mutableListOf<ILineDataSet>()
        dataSets.add(dataSet)
        val data: LineData = LineData(dataSets)

        chart.description.isEnabled = true
        val description = Description()
        description.text = "Day"
        description.textSize = 10f
        description.textColor = whiteColor
        chart.description = description

        val xAxis = chart.xAxis
        xAxis.labelCount = 5
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawLabels(true)
        xAxis.valueFormatter = DateYAxisValueFormatter()
        xAxis.labelRotationAngle = 315f
        xAxis.textColor = redColor

        val yAxis = chart.axisLeft
        yAxis.valueFormatter = WeightYAxisValueFormatter()
        yAxis.setDrawLabels(true)
        yAxis.textColor = redColor

        chart.legend.textColor = redColor
        chart.legend.yOffset = 10f
        chart.axisRight.isEnabled = false
        chart.setExtraOffsets(5f,5f,5f,15f)
        chart.data = data
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}

class WeightYAxisValueFormatter : ValueFormatter() {
    override fun getAxisLabel(value: Float, axis: AxisBase): String {
        return "$value lbs"
    }
}

class DateYAxisValueFormatter : ValueFormatter() {
    val sdf = SimpleDateFormat("MMM dd yy")
    override fun getAxisLabel(value: Float, axis: AxisBase): String {
        return sdf.format(Date(value.toLong()))
    }
}

