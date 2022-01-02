package com.example.workaudio.presentation.navigation

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
import com.bumptech.glide.Glide
import com.example.workaudio.R
import com.example.workaudio.databinding.FragmentWorkoutListBinding
import com.example.workaudio.core.entities.Workout


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
        initLayoutFunctionality()
        initObservers()
        return binding.root
    }

    private fun initLayoutFunctionality() {
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

    private fun initObservers() {
        viewModel.workouts.observe(this, { workouts ->
            workouts?.let {
                workoutAdapter.apply {
                    updateWorkouts(it)
                }

            }
        })
    }

    private fun provideAdapter() = WorkoutAdapter(
        mutableListOf<Workout>(),
        openWorkoutDetail = { workoutId ->
            navigateToFragment(
                R.id.action_workoutListFragment_to_workoutDetailFragment,
                workoutId
            )
        },
        showBottomModal = { workoutId ->
            showModalBottomFragment(workoutId)
        },
        fetchImage = { imageView, imageUri ->
            Glide.with(requireActivity()).load(imageUri).into(imageView)
        }
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

    private fun showModalBottomFragment(workoutId: Int) {
        val modalBottomSheet = BottomModalSelectWorkout(workoutId){ id ->
            viewModel.deleteWorkout(id)
        }
        modalBottomSheet.show(parentFragmentManager, BottomModalSelectWorkout.TAG)
    }

}