package com.example.workaudio.presentation.creation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.workaudio.R
import com.example.workaudio.databinding.FragmentDurationBinding
import com.example.workaudio.presentation.NavigationManager


class DurationFragment : Fragment() {

    companion object {
        const val ID_TAG = "id"
        const val WORKOUT_NAME = "workout_name"
        const val WORKOUT_DURATION = "workout_duration"
        private const val DEFAULT_DURATION = 30.0f
        private const val DURATION_TO_NAME = R.id.action_durationFragment_to_nameFragment
        private const val DURATION_TO_DETAIL = R.id.action_durationFragment_to_workoutDetailFragment
    }

    private lateinit var binding: FragmentDurationBinding
    private val viewModel: DurationFragmentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDurationBinding.inflate(inflater, container, false)
        setupLayoutFunctionality()
        setupObserver()
        return binding.root
    }

    private fun setupObserver(){
        viewModel.workout.observe(this,{ workout ->
            if(viewModel.state == DurationFragmentViewModel.Companion.STATE.CREATED){
                val bundle = bundleOf(ID_TAG to workout.id)
                NavigationManager.navigateTo(findNavController(),DURATION_TO_DETAIL,bundle)
            }
        })
    }

    private fun setupLayoutFunctionality() {

        binding.apply {

            cancelButton.setOnClickListener {
                NavigationManager.navigateTo(findNavController(), DURATION_TO_NAME)
            }
            continueButton.setOnClickListener {
                val workoutName = arguments?.getString(WORKOUT_NAME).orEmpty()
                val duration = binding.rangeSlider.values[0].toInt()
                viewModel.insertWorkout(workoutName, duration)
            }

            timeLabel.text = requireActivity().resources.getString(R.string.default_duration)

            rangeSlider.apply {
                setValues(DEFAULT_DURATION)
                addOnChangeListener { _, duration, _ ->
                    timeLabel.text = viewModel.formatDuration(duration)
                }
            }

        }
    }

}