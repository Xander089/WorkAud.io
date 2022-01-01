package com.example.workaudio.presentation.creation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.workaudio.R
import com.example.workaudio.databinding.FragmentDurationBinding


class DurationFragment : Fragment() {

    companion object {
        private const val WORKOUT_NAME = "workout_name"
        private const val WORKOUT_DURATION = "workout_duration"
    }

    private lateinit var binding: FragmentDurationBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDurationBinding.inflate(inflater, container, false)

        binding.apply {
            rangeSlider.setValues(30.0f)
            timeLabel.text = "30''"

            cancelWorkoutCreationButton.setOnClickListener {
                //on back pressed
            }

            continueButton.setOnClickListener {
                arguments?.getString(WORKOUT_NAME).orEmpty().let { name ->
                    if(name.isNotEmpty()){
                        val duration = binding.rangeSlider.values[0].toInt()
                        findNavController().navigate(
                            R.id.action_durationFragment_to_tracksFragment,
                            bundleOf(WORKOUT_NAME to name, WORKOUT_DURATION to duration)
                        )
                    }

                }
            }

            rangeSlider.addOnChangeListener { _, value, _ ->
                timeLabel.text = "${value.toInt()}''"
            }
        }

        return binding.root
    }

}