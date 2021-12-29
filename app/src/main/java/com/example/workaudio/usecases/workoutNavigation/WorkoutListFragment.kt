package com.example.workaudio.usecases.workoutNavigation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workaudio.R
import com.example.workaudio.WorkoutAdapter
import com.example.workaudio.databinding.FragmentWorkoutListBinding
import com.example.workaudio.entities.Track
import com.example.workaudio.entities.Workout


class WorkoutListFragment : Fragment() {

    private lateinit var binding: FragmentWorkoutListBinding
    private lateinit var workoutAdapter: WorkoutAdapter
    private val viewModel: WorkoutNavigationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkoutListBinding.inflate(inflater, container, false)
        workoutAdapter = provideAdapter()
        setLayoutFunctionality()
        setObservers()
        return binding.root
    }

    private fun setObservers() {
        viewModel.workouts.observe(this, { workouts ->
            Log.v("workouts", workouts.toString())
            workouts?.let {
                workoutAdapter.apply {
                    updateWorkouts(it)
                    notifyDataSetChanged()
                }

            }
        })
    }

    private fun setLayoutFunctionality() {
        binding.apply {
            workoutList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = workoutAdapter
            }

            createWorkoutButton.setOnClickListener {
                navigateToFragment(R.id.action_workoutListFragment_to_nameFragment)
            }
        }
    }

    private fun provideAdapter() = WorkoutAdapter(
        mutableListOf<Workout>(),
        startWorkout = { workoutId ->
            navigateToFragment(
                R.id.action_workoutListFragment_to_workoutDetailFragment,
                workoutId
            )
        },
        deleteWorkout = {},
        bottomModalLambda = {}
    )

    private fun navigateToFragment(
        fragmentId: Int,
        workoutId: Int = 0
    ) {
        findNavController().navigate(
            fragmentId,
            bundleOf("id" to workoutId)
        )
    }

}