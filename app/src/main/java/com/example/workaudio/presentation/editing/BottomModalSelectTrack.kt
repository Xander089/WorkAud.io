package com.example.workaudio.presentation.editing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.workaudio.databinding.ModalBottomLayoutBinding
import com.example.workaudio.Constants.LABEL
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomModalSelectTrack(
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

}

