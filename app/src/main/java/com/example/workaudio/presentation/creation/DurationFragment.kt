package com.example.workaudio.presentation.creation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.workaudio.R
import com.example.workaudio.databinding.FragmentDurationBinding


class DurationFragment : Fragment() {

    companion object {
        const val WORKOUT_NAME = "workout_name"
        private const val WORKOUT_DURATION = "workout_duration"
        private const val DEFAULT_DURATION = 30.0f
    }

    private lateinit var binding: FragmentDurationBinding
    private val viewModel : DurationFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDurationBinding.inflate(inflater, container, false)

        initLayoutFunctionality()

        return binding.root
    }

    private fun initLayoutFunctionality() {
        val workoutName = arguments?.getString(WORKOUT_NAME).orEmpty()
        binding.apply {
            rangeSlider.setValues(DEFAULT_DURATION)
            timeLabel.text = requireActivity().resources.getString(R.string.default_duration)

            cancelButton.setOnClickListener {
                navigateTo(
                    R.id.action_durationFragment_to_nameFragment
                )
            }

            continueButton.setOnClickListener {
                val duration = binding.rangeSlider.values[0].toInt()
                navigateTo(
                    R.id.action_durationFragment_to_tracksFragment,
                    bundleOf(WORKOUT_NAME to workoutName, WORKOUT_DURATION to duration)
                )
            }


            rangeSlider.addOnChangeListener { _, duration, _ ->
                timeLabel.text = viewModel.formatDuration(duration)
            }
        }
    }

    private fun navigateTo(
        action: Int,
        bundle: Bundle? = null
    ) {
        if (bundle == null) {
            findNavController().navigate(
                action
            )
        } else {
            findNavController().navigate(
                action,
                bundle
            )
        }

    }

}