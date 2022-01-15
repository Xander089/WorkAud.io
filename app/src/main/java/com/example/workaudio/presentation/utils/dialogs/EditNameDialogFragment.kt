package com.example.workaudio.presentation.utils.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.workaudio.R

class EditNameDialogFragment(
    val updateWorkout: (name: String) -> Unit
) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(R.layout.dialog_name, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = layoutParams
        val cancelButton = view.findViewById<Button>(R.id.cancelButton)
        val confirmNameButton = view.findViewById<Button>(R.id.loginButton)
        val workoutNameText = view.findViewById<EditText>(R.id.workoutNameText)

        cancelButton.setOnClickListener {
            dialog?.dismiss()
        }
        confirmNameButton.setOnClickListener {
            val newName = workoutNameText.text.toString()
            updateWorkout(newName)
            dialog?.dismiss()
        }

    }
}