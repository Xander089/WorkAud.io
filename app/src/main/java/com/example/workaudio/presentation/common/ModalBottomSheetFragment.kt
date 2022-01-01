package com.example.workaudio.presentation.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.workaudio.databinding.ModalBottomLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ModalBottomSheetFragment(
    val workoutId: Int,
    val deleteWorkout: (Int) -> Unit,
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = ModalBottomLayoutBinding.inflate(inflater, container, false)

        binding.apply {
            deleteWorkoutButton.setOnClickListener {
                deleteWorkout(workoutId)
            }
        }

        return binding.root
    }


    companion object {
        const val TAG = "ModalBottomSheet"
    }


}

