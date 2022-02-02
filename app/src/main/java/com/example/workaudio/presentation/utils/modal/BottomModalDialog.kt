package com.example.workaudio.presentation.utils.modal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.workaudio.common.Constants.MODAL_ACTION
import com.example.workaudio.common.Constants.MODAL_TITLE
import com.example.workaudio.databinding.ModalLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomModalDialog(

) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val binding = ModalLayoutBinding.inflate(inflater, container, false)

        binding.apply {
            root.setOnClickListener {
                getAction()?.onClick()
                dismiss()
            }
            deleteText.text = getTitle()
        }

        return binding.root
    }

    private fun getTitle() = arguments?.getString(MODAL_TITLE)
    private fun getAction() = arguments?.getParcelable<ModalAction>(MODAL_ACTION)


}

