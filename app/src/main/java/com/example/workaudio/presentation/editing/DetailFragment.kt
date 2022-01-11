package com.example.workaudio.presentation.editing

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.workaudio.R
import com.example.workaudio.databinding.FragmentWorkoutDetailBinding
import com.example.workaudio.presentation.Constants.DURATION_TAG
import com.example.workaudio.presentation.Constants.ID_TAG
import com.example.workaudio.presentation.Constants.NAME_TAG
import com.example.workaudio.presentation.Constants.TAG
import com.example.workaudio.presentation.player.PlayerActivity
import com.example.workaudio.presentation.dialogs.EditDurationDialogFragment
import com.example.workaudio.presentation.dialogs.EditNameDialogFragment
import com.example.workaudio.presentation.NavigationManager


class DetailFragment : Fragment() {

    companion object {
        private const val DETAIL_TO_EDITING_TRACKS =
            R.id.action_workoutDetailFragment_to_editingTracksFragment
        private const val DETAIL_TO_WORKOUTS =
            R.id.action_workoutDetailFragment_to_workoutListFragment
    }

    private lateinit var binding: FragmentWorkoutDetailBinding
    private lateinit var workoutAdapter: DetailTracksAdapter
    private val viewModel: DetailFragmentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentWorkoutDetailBinding.inflate(inflater, container, false)

        buildAdapter()
        viewModel.initializeCurrentWorkout(getWorkoutId())
        setLayoutFunctionality()
        setObservers()

        return binding.root
    }

    private fun buildAdapter() {
        workoutAdapter = DetailTracksAdapter(
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
            .show(parentFragmentManager, TAG)
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

            infoButton.setOnClickListener {
                Toast.makeText(
                    activity,
                    "you need to match at least the target duration to start the playlist",
                    Toast.LENGTH_SHORT
                )
                    .show()
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
            handlePlayButton(viewModel.tracksDurationCheck(tracks))
            binding.durationText.text = viewModel.getTracksDuration(tracks)
            workoutAdapter.refreshTrackList(tracks)
        })

    }

    private fun handlePlayButton(enabled: Boolean) {
        binding.playButton.isEnabled = enabled
        if (enabled) {
            binding.playButton.setStrokeColorResource(R.color.yellow)
            binding.playButton.setTextColor(getColor(R.color.yellow))
        } else {
            binding.playButton.setStrokeColorResource(R.color.grey2)
            binding.playButton.setTextColor(getColor(R.color.grey2))
        }

    }

    private fun getColor(id: Int) = requireActivity().resources.getColor(id, null)

    private fun startPlayerActivity() {
        startActivity(
            PlayerActivity
                .newIntent(
                    requireContext(),
                    getWorkoutId()
                )
        )
    }


    private fun showEditNameDialogFragment() {
        EditNameDialogFragment { name ->
            viewModel.updateWorkoutName(getWorkoutId(), name)
        }
            .show(parentFragmentManager, NAME_TAG)
    }

    private fun showEditDurationDialogFragment() {
        EditDurationDialogFragment { duration ->
            viewModel.updateWorkoutDuration(getWorkoutId(), duration)
        }
            .show(parentFragmentManager, DURATION_TAG)
    }

    private fun getWorkoutId() = arguments?.getInt(ID_TAG) ?: -1

}