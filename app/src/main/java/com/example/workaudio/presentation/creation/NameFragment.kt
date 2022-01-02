package com.example.workaudio.presentation.creation

import android.os.Bundle
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.workaudio.R
import com.example.workaudio.databinding.FragmentNameBinding
import android.text.Editable


class NameFragment : Fragment() {

    private lateinit var binding: FragmentNameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentNameBinding.inflate(inflater, container, false)

        binding.apply {

            confirmNameButton.apply {
                isEnabled = false
                setTextColor(
                    requireActivity()
                        .resources
                        .getColor(R.color.grey2, null)
                )
                setStrokeColorResource(R.color.grey2)
                setOnClickListener {
                    val workoutName = workoutNameText.text.toString()
                    navigateTo(
                        R.id.action_nameFragment_to_durationFragment,
                        bundleOf(DurationFragment.WORKOUT_NAME to workoutName)
                    )
                }

                cancelButton.setOnClickListener {
                    navigateTo(
                        R.id.action_nameFragment_to_workoutListFragment
                    )
                }
                workoutNameText.addTextChangedListener(object : TextWatcher {
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
                        if (s.isNotEmpty()) {
                            confirmNameButton.apply {
                                isEnabled = true
                                setTextColor(
                                    requireActivity()
                                        .resources
                                        .getColor(R.color.yellow, null)
                                )
                                setStrokeColorResource(R.color.yellow)
                            }
                        } else {
                            confirmNameButton.apply {
                                isEnabled = false
                                setTextColor(
                                    requireActivity()
                                        .resources
                                        .getColor(R.color.grey2, null)
                                )
                                setStrokeColorResource(R.color.grey2)
                            }
                        }
                    }
                })
            }
        }

        return binding.root
    }

    private fun navigateTo(
        action: Int,
        bundle: Bundle? = null
    ) {
        if (bundle == null) {
            findNavController().navigate(
                action
            )
        } else {
            findNavController().navigate(
                action,
                bundle
            )
        }

    }

}