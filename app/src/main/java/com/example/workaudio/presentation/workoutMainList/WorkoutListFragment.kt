package com.example.workaudio.presentation.workoutMainList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.workaudio.Constants.ID_TAG
import com.example.workaudio.R
import com.example.workaudio.databinding.FragmentWorkoutListBinding
import com.example.workaudio.Constants.TAG
import com.example.workaudio.presentation.utils.itemTouchHelper.NavigationManager
import com.example.workaudio.presentation.utils.adapter.AdapterFactory
import com.example.workaudio.presentation.utils.adapter.AdapterFlavour
import com.example.workaudio.presentation.utils.adapter.WorkoutAdapter


class WorkoutListFragment : Fragment() {

    companion object {
        private const val WORKOUTS_TO_NAME = R.id.action_workoutListFragment_to_nameFragment
        private const val WORKOUTS_TO_DETAIL = R.id.action_workoutListFragment_to_workoutDetailFragment
    }

    private lateinit var binding: FragmentWorkoutListBinding
    private lateinit var workoutAdapter: WorkoutAdapter
    private val viewModel: WorkoutListFragmentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkoutListBinding.inflate(inflater, container, false)
        workoutAdapter = provideAdapter() as WorkoutAdapter
        setupLayoutFunctionality()
        setupObservers()
        return binding.root
    }

    private fun setupLayoutFunctionality() {
        binding.apply {
            workoutList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = workoutAdapter
            }

            createWorkoutButton.setOnClickListener {
                NavigationManager.navigateTo(findNavController(), WORKOUTS_TO_NAME)
            }
        }
    }

    private fun setupObservers() {
        viewModel.workouts.observe(this, { workouts ->
            workouts?.let {
                workoutAdapter.updateWorkouts(it)
            }
        })
    }

    private fun provideAdapter() = AdapterFactory.create(
        AdapterFlavour.WORKOUT,
        openWorkoutDetail = { workoutId ->
            val bundle = bundleOf(ID_TAG to workoutId)
            NavigationManager.navigateTo(findNavController(), WORKOUTS_TO_DETAIL, bundle)
        },
        showBottomModal = { workoutId ->
            showModalBottomFragment(workoutId)
        },
        fetchImage = { imageView, imageUri ->
            fetchImage(imageView,imageUri)
        }
    )

    private fun fetchImage(imageView: ImageView, imageUri: String){
        if(imageUri.isEmpty()){
            return
        }
        Glide.with(requireActivity()).load(imageUri).into(imageView)
    }


    private fun showModalBottomFragment(workoutId: Int) {
        val modalBottomSheet = BottomModalSelectWorkout(workoutId) { id ->
            viewModel.deleteWorkout(id)
        }
        modalBottomSheet.show(parentFragmentManager, TAG)
    }

}