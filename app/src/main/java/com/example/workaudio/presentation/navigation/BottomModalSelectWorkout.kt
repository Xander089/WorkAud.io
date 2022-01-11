package com.example.workaudio.presentation.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.workaudio.databinding.ModalBottomLayoutBinding
import com.example.workaudio.presentation.Constants.LABEL
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomModalSelectWorkout(
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
            root.setOnClickListener {
                deleteWorkout(workoutId)
                dismiss()
            }
            deleteText.text = LABEL
        }

        return binding.root
    }

}

