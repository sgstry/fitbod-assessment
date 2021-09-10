package com.android.example.brzycki_formula.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.android.example.brzycki_formula.ExerciseHistoryActivity
import com.android.example.brzycki_formula.R
import com.android.example.brzycki_formula.database.ExerciseDatabase
import com.android.example.brzycki_formula.databinding.ExerciseHistoryFragmentBinding
import com.android.example.brzycki_formula.databinding.MainFragmentBinding

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

        binding.lifecycleOwner = this
        binding.historyViewModel = viewModel

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ExerciseHistoryViewModel::class.java)
        // TODO: Use the ViewModel
    }

}