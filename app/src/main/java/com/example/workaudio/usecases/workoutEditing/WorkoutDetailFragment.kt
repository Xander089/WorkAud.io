package com.example.workaudio.usecases.workoutEditing

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workaudio.R
import com.example.workaudio.WorkoutAdapter
import com.example.workaudio.WorkoutDetailTracksAdapter
import com.example.workaudio.databinding.FragmentWorkoutDetailBinding
import com.example.workaudio.databinding.FragmentWorkoutListBinding
import com.example.workaudio.entities.Track
import com.example.workaudio.usecases.player.PlayerActivity

class WorkoutDetailFragment : Fragment() {

    private lateinit var binding: FragmentWorkoutDetailBinding
    private lateinit var _adapter: WorkoutDetailTracksAdapter
    private val viewModel: WorkoutEditingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkoutDetailBinding.inflate(inflater, container, false)
        _adapter = WorkoutDetailTracksAdapter(mutableListOf<Track>(), {})

        arguments?.getInt("id")?.let { workoutId ->
            viewModel.setSelectedWorkout(workoutId)
        }

        setLayoutFunctionality()
        setObservers()
        return binding.root
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
                arguments?.getInt("id")?.let { workoutId ->
                    navigateToFragment(
                        R.id.action_workoutDetailFragment_to_editingTracksFragment,
                        workoutId
                    )
                }
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
        arguments?.getInt("id")?.let { workoutId ->
            val intent = PlayerActivity.newIntent(requireContext(), workoutId)
            startActivity(intent)
        }
    }

    private fun navigateToFragment(
        fragmentId: Int,
        workoutId: Int = 0
    ) {
        findNavController().navigate(
            fragmentId,
            bundleOf("id" to workoutId)
        )
    }

}