package com.example.workaudio.presentation.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.workaudio.R
import com.google.android.material.slider.RangeSlider

class EditDurationDialogFragment(
    val updateDuration: (duration: Int) -> Unit
) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(R.layout.dialog_duration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = layoutParams
        val cancelButton = view.findViewById<Button>(R.id.cancelButton)
        val confirmButton = view.findViewById<Button>(R.id.continueButton)
        val durationRangeSlider = view.findViewById<RangeSlider>(R.id.rangeSlider)
        val timeLabel = view.findViewById<TextView>(R.id.timeLabel)

        cancelButton.setOnClickListener {
            dialog?.dismiss()
        }
        confirmButton.setOnClickListener {
            val newDuration = (durationRangeSlider.values[0] ?: 0.0f).toInt()
            updateDuration(newDuration)
            dialog?.dismiss()
        }
        durationRangeSlider.addOnChangeListener { _, value, _ ->
            timeLabel.text = value.toInt().toString().plus(MINUTES)
        }

    }

    companion object {
        const val TAG = "EditDurationDialog"
        const val MINUTES = " min"
    }
}