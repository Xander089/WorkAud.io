package com.example.workaudio.presentation.editing

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.workaudio.R
import com.example.workaudio.databinding.FragmentWorkoutDetailBinding
import com.example.workaudio.core.entities.Track
import com.example.workaudio.presentation.player.PlayerActivity
import com.example.workaudio.dialogs.EditDurationDialogFragment
import com.example.workaudio.dialogs.EditNameDialogFragment
import com.example.workaudio.presentation.NavigationManager
import com.example.workaudio.presentation.navigation.BottomModalSelectWorkout


class WorkoutDetailFragment : Fragment() {

    companion object {
        const val ID_TAG = "id"
        private const val DETAIL_TO_EDITING_TRACKS =
            R.id.action_workoutDetailFragment_to_editingTracksFragment
        private const val DETAIL_TO_WORKOUTS =
            R.id.action_workoutDetailFragment_to_workoutListFragment
    }

    private lateinit var binding: FragmentWorkoutDetailBinding
    private lateinit var workoutAdapter: WorkoutDetailTracksAdapter
    private val viewModel: WorkoutDetailFragmentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentWorkoutDetailBinding.inflate(inflater, container, false)

        buildAdapter()
        refreshCurrentWorkout()
        setLayoutFunctionality()
        setObservers()

        return binding.root
    }

    private fun refreshCurrentWorkout() {
        val workoutId = getWorkoutId()
        viewModel.initializeCurrentWorkout(workoutId)
    }

    private fun buildAdapter() {
        workoutAdapter = WorkoutDetailTracksAdapter(
            mutableListOf<Track>(),
            fetchImage = { imageView, imageUri ->
                Glide.with(requireActivity()).load(imageUri).into(imageView)
            },
            deleteTrack = { uri ->
                showModalBottomFragment(uri)
            }
        )
    }

    private fun showModalBottomFragment(trackUri: String) {
        BottomModalSelectTrack(trackUri) { uri ->
            viewModel.deleteTrack(uri)
        }
            .show(parentFragmentManager, BottomModalSelectWorkout.TAG)
    }


    private fun setLayoutFunctionality() {
        binding.apply {
            trackList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = workoutAdapter
            }
            playButton.setOnClickListener {
                startPlayerActivity()
            }
            editTracksButton.setOnClickListener {
                val workoutId = getWorkoutId()
                val bundle = bundleOf(ID_TAG to workoutId)
                NavigationManager.navigateTo(findNavController(), DETAIL_TO_EDITING_TRACKS, bundle)

            }
            workoutName.setOnClickListener {
                showEditNameDialogFragment()
            }
            durationText.setOnClickListener {
                showEditDurationDialogFragment()
            }
            backButton.setOnClickListener {
                NavigationManager.navigateTo(findNavController(), DETAIL_TO_WORKOUTS)
            }
        }
    }

    private fun setObservers() {
        viewModel.selectedWorkout.observe(this, { workout ->

            binding.apply {
                workoutName.text = workout.name
                durationText.text = viewModel.durationToMinutes(workout.duration)
            }
        })

        viewModel.tracks.observe(this, { tracks ->
            workoutAdapter.tracks.apply {
                clear()
                addAll(tracks)
            }
            workoutAdapter.notifyDataSetChanged()
        })

    }

    private fun startPlayerActivity() {
        val intent = PlayerActivity.newIntent(requireContext(), getWorkoutId())
        startActivity(intent)
    }


    private fun showEditNameDialogFragment() {
        EditNameDialogFragment { name ->
            viewModel.updateWorkoutName(getWorkoutId(), name)
        }
            .show(parentFragmentManager, EditNameDialogFragment.TAG)
    }

    private fun showEditDurationDialogFragment() {
        EditDurationDialogFragment { duration ->
            viewModel.updateWorkoutDuration(getWorkoutId(), duration)
        }
            .show(parentFragmentManager, EditDurationDialogFragment.TAG)
    }

    private fun getWorkoutId() = arguments?.getInt(ID_TAG) ?: -1

}