package com.example.workaudio.presentation.utils.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.workaudio.R

class StopPlayerDialogFragment(
    val ok: () -> Unit,
    val cancel: () -> Unit = {}
) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(R.layout.dialog_stop_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = layoutParams
        val cancelButton = view.findViewById<Button>(R.id.cancelButton)
        val confirmButton = view.findViewById<Button>(R.id.continueButton)

        cancelButton.setOnClickListener {
            cancel()
            dialog?.dismiss()
        }
        confirmButton.setOnClickListener {
            ok()
            dialog?.dismiss()
        }

    }

}