package com.example.workaudio.presentation.workoutCreation

import android.content.Context
import android.os.Bundle
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.workaudio.R
import com.example.workaudio.databinding.FragmentNameBinding
import android.text.Editable
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import com.example.workaudio.Constants.WORKOUT_NAME
import com.example.workaudio.presentation.utils.NavigationManager


class NameFragment : Fragment() {

    companion object {
        private const val NAME_TO_DURATION = R.id.action_nameFragment_to_durationFragment
        private const val NAME_TO_WORKOUTS = R.id.action_nameFragment_to_workoutListFragment
        private const val ENABLED_COLOR = R.color.yellow
        private const val DISABLED_COLOR = R.color.grey2
    }

    private lateinit var binding: FragmentNameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentNameBinding.inflate(inflater, container, false)
        setupLayoutFunctionality()
        return binding.root
    }

    private fun showSoftKeyboard(view: View) {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun setupLayoutFunctionality() {
        binding.apply {

            workoutNameText.apply {
                setOnFocusChangeListener { view, b ->
                    showSoftKeyboard(view)
                }
                requestFocus()
            }

            confirmNameButton.apply {
                setOnClickListener {
                    val workoutName = workoutNameText.text.toString()
                    val bundle = bundleOf(WORKOUT_NAME to workoutName)

                    NavigationManager.navigateTo(
                        findNavController(),
                        NAME_TO_DURATION,
                        bundle
                    )
                }
            }
            cancelButton.setOnClickListener {
                NavigationManager.navigateTo(findNavController(), NAME_TO_WORKOUTS)
            }
            workoutNameText.addTextChangedListener(listener)
        }
    }

    private val listener = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {}
        override fun beforeTextChanged(
            s: CharSequence, start: Int,
            count: Int, after: Int
        ) {
        }

        override fun onTextChanged(
            s: CharSequence, start: Int,
            before: Int, count: Int
        ) {
            if (s.length > 4) {
                binding.confirmNameButton.apply {
                    isEnabled = true
                    setTextColor(
                        requireActivity().resources.getColor(ENABLED_COLOR, null)
                    )
                    setStrokeColorResource(ENABLED_COLOR)
                }
            } else {
                binding.confirmNameButton.apply {
                    isEnabled = false
                    setTextColor(
                        requireActivity().resources.getColor(DISABLED_COLOR, null)
                    )
                    setStrokeColorResource(DISABLED_COLOR)
                }
            }
        }
    }
}