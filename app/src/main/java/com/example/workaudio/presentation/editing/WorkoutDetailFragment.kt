package com.example.workaudio.presentation.editing

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.workaudio.R
import com.example.workaudio.databinding.FragmentWorkoutDetailBinding
import com.example.workaudio.core.entities.Track
import com.example.workaudio.presentation.player.PlayerActivity
import android.widget.TextView
import com.example.workaudio.presentation.navigation.BottomModalSelectWorkout
import com.google.android.material.slider.RangeSlider


class WorkoutDetailFragment : Fragment() {

    companion object {
        const val ID_TAG = "id"
    }

    private lateinit var binding: FragmentWorkoutDetailBinding
    private lateinit var _adapter: WorkoutDetailTracksAdapter
    private val viewModel: WorkoutEditingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentWorkoutDetailBinding.inflate(inflater, container, false)
        _adapter = WorkoutDetailTracksAdapter(
            mutableListOf<Track>(),
            fetchImage = { imageView, imageUri ->
                Glide.with(requireActivity()).load(imageUri).into(imageView)
            },
            deleteTrack = { uri ->
                showModalBottomFragment(uri)
            },
            selectedColorId = resources.getColor(R.color.grey3,null)
        )

        setupCurrentWorkout()
        setLayoutFunctionality()
        setObservers()
        return binding.root
    }

    private fun showModalBottomFragment(trackUri: String) {
        val modalBottomSheet = BottomModalSelectTrack(trackUri){ uri ->
            viewModel.deleteTrack(trackUri)
        }
        modalBottomSheet.show(parentFragmentManager, BottomModalSelectWorkout.TAG)
    }

    private fun setupCurrentWorkout() {
        arguments?.getInt(ID_TAG)?.let { workoutId ->
            viewModel.initializeCurrentWorkout(workoutId)
        }
    }

    private fun setLayoutFunctionality() {
        binding.apply {
            trackList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = _adapter
            }
            playButton.setOnClickListener {
                startPlayerActivity()
            }
            editTracksButton.setOnClickListener {
                arguments?.getInt(ID_TAG)?.let { workoutId ->
                    navigateTo(
                        R.id.action_workoutDetailFragment_to_editingTracksFragment,
                        bundleOf(ID_TAG to workoutId)
                    )
                }
            }
            workoutName.setOnClickListener {
                showEditNameDialogFragment()
            }
            durationText.setOnClickListener {
                showEditDurationDialogFragment()
            }
            backButton.setOnClickListener {
                navigateTo(
                    R.id.action_workoutDetailFragment_to_workoutListFragment
                )
            }
        }
    }

    private fun setObservers() {
        viewModel.selectedWorkout.observe(this, { workout ->
            binding.apply {
                workoutName.text = workout.name
                durationText.text = (workout.duration / 60000).toString()
            }
        })

        viewModel.tracks.observe(this, { trackList ->
            _adapter.tracks.apply {
                clear()
                addAll(trackList)
            }
            _adapter.notifyDataSetChanged()
        })
    }

    private fun startPlayerActivity() {
        arguments?.getInt(ID_TAG)?.let { workoutId ->
            val intent = PlayerActivity.newIntent(requireContext(), workoutId)
            startActivity(intent)
        }
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



    private fun showEditNameDialogFragment() {
        val dialog = EditNameDialogFragment { name ->
            viewModel.updateWorkoutName(name)
            setupCurrentWorkout()
        }

        dialog.show(parentFragmentManager, EditNameDialogFragment.TAG)
    }

    private fun showEditDurationDialogFragment() {
        val dialog = EditDurationDialogFragment { duration ->
            viewModel.updateWorkoutDuration(duration)
            setupCurrentWorkout()
        }

        dialog.show(parentFragmentManager, EditDurationDialogFragment.TAG)
    }

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

        companion object {
            const val TAG = "EditNameDialog"
        }
    }

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

}