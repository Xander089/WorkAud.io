package com.example.workaudio.usecases.workoutNavigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkoutListBinding.inflate(inflater, container, false)
        val workout = Workout(
            name = "pippo",
            currentDuration = 0,
            duration = 1800,
            tracks = emptyList(),
        )

        workoutAdapter = WorkoutAdapter(
            mutableListOf<Workout>(workout),
            startWorkout = { workoutId ->
                navigateToFragment(
                    R.id.action_workoutListFragment_to_workoutDetailFragment,
                    workoutId
                )
            },
            deleteWorkout = {},
            bottomModalLambda = {}
        )

        binding.apply {
            workoutList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = workoutAdapter
            }

            createWorkoutButton.setOnClickListener {
                navigateToFragment(R.id.action_workoutListFragment_to_nameFragment)
            }
        }

        return binding.root
    }

    private fun navigateToFragment(
        fragmentId: Int,
        workoutId: Int = 0
    ) {
        //
        findNavController().navigate(
            fragmentId,
            bundleOf("id" to workoutId)
        )
    }

}