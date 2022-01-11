package com.example.workaudio.presentation.creation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.workaudio.R
import com.example.workaudio.databinding.FragmentDurationBinding
import com.example.workaudio.presentation.Constants.DEFAULT_DURATION
import com.example.workaudio.presentation.Constants.ID_TAG
import com.example.workaudio.presentation.Constants.WORKOUT_NAME
import com.example.workaudio.presentation.NavigationManager


class DurationFragment : Fragment() {

    companion object {
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

    private fun setupObserver() {
        viewModel.workout.observe(this, { workout ->
            if (viewModel.isWorkoutCreated()) {
                NavigationManager.navigateTo(
                    findNavController(),
                    DURATION_TO_DETAIL,
                    bundleOf(ID_TAG to workout?.id)
                )
            }
        })
    }

    private fun setupLayoutFunctionality() {

        binding.apply {

            cancelButton.setOnClickListener {
                NavigationManager.navigateTo(
                    findNavController(),
                    DURATION_TO_NAME
                )
            }
            continueButton.setOnClickListener {
                viewModel.insertWorkout(
                    getWorkoutName(),
                    getRangeSliderDuration()
                )
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

    private fun getWorkoutName() = arguments?.getString(WORKOUT_NAME).orEmpty()
    private fun getRangeSliderDuration() = binding.rangeSlider.values[0].toInt()

}