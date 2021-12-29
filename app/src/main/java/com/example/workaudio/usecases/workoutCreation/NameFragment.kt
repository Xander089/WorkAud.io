package com.example.workaudio.usecases.workoutCreation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.workaudio.R
import com.example.workaudio.databinding.FragmentNameBinding

class NameFragment : Fragment() {

    private lateinit var binding: FragmentNameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        binding = FragmentNameBinding.inflate(inflater, container, false)

        binding.apply {

            confirmNameButton.setOnClickListener {
                val workoutName = workoutNameText.text.toString()
                findNavController().navigate(
                    R.id.action_nameFragment_to_durationFragment,
                    bundleOf("workout_name" to workoutName)
                )
            }
        }

        return binding.root
    }

}