package com.android.example.brzycki_formula.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.android.example.brzycki_formula.R
import com.android.example.brzycki_formula.database.ExerciseDatabase
import com.android.example.brzycki_formula.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: MainFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.main_fragment, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = ExerciseDatabase.getInstance(application).exerciseDatabaseDao
        val viewModelFactory = MainViewModelFactory(dataSource, application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        binding.lifecycleOwner = this
        binding.mainViewModel = viewModel

        val adapter = ExerciseAdapter()
        binding.exerciseList.adapter = adapter

        viewModel.exercises.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
            }
        })


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}