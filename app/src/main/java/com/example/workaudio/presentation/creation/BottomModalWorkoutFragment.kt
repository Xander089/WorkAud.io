package com.example.workaudio.presentation.creation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.workaudio.databinding.ModalBottomLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomModalWorkoutFragment(
    private val trackUri: String,
    val deleteTrack: (String) -> Unit,
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = ModalBottomLayoutBinding.inflate(inflater, container, false)

        binding.apply {
            root.setOnClickListener {
                deleteTrack(trackUri)
                dismiss()
            }
            deleteText.text = LABEL
        }

        return binding.root
    }


    companion object {
        private const val LABEL = "Delete Selected Track"
        const val TAG = "ModalBottomSheet"
    }


}

