package com.example.workaudio.presentation.creation

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.workaudio.R
import com.example.workaudio.databinding.FragmentTracksBinding
import com.example.workaudio.core.entities.Track


class TracksFragment : Fragment() {

    companion object {
        private const val WORKOUT_NAME = "workout_name"
        private const val WORKOUT_DURATION = "workout_duration"
    }

    private lateinit var binding: FragmentTracksBinding
    private lateinit var trackListAdapter: WorkoutTracksAdapter
    private val viewModel: WorkoutCreationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTracksBinding.inflate(layoutInflater, container, false)

        cacheWorkoutInfo()
        buildAdapter()
        initLayout()
        initObservers()

        return binding.root
    }

    private fun buildAdapter(){
        trackListAdapter = WorkoutTracksAdapter(
            mutableListOf<Track>(),
            addTrack = { track, isAdded ->
                viewModel.addTrack(track)
            },
            fetchImage = { imageView, imageUri ->
                Glide.with(requireActivity()).load(imageUri).into(imageView)
            }
        )
    }

    private fun initLayout(){
        binding.apply {
            (activity as AppCompatActivity?)!!.setSupportActionBar(topAppBar)
            minuteLabel.text = "0/${(arguments?.getInt(WORKOUT_DURATION) ?: 0)} min"
            saveButton.isEnabled = false
            saveButton.setOnClickListener {
                viewModel.createWorkout()
                findNavController().navigate(R.id.action_tracksFragment_to_workoutListFragment)
            }
            trackList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = trackListAdapter
            }
        }
    }

    private fun initObservers(){
        viewModel.searchedTracks.observe(this, { searchedTracks ->
            trackListAdapter.apply {
                tracks.clear()
                tracks.addAll(searchedTracks)
                notifyDataSetChanged()
            }
        })

        viewModel.currentDuration.observe(this, { duration ->
            binding.apply {
                saveButton.isEnabled = viewModel.compareDuration()
                minuteLabel.text = (duration / 60000).toString()
            }
        })
    }

    private fun cacheWorkoutInfo() {
        val name = arguments?.getString(WORKOUT_NAME).orEmpty()
        val duration = arguments?.getInt(WORKOUT_DURATION) ?: 0
        viewModel.cacheWorkoutInfo(name, duration)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        var searchView: SearchView? = null
        menu.findItem(R.id.action_search)?.also {
            searchView = it.actionView as SearchView
        }

        val searchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchView?.apply {
            setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    viewModel.searchTracks(newText)
                    return true
                }

                override fun onQueryTextSubmit(query: String) = true
            })
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> navigateTo(
                R.id.action_tracksFragment_to_durationFragment2
            )
            else -> {}
        }

        return super.onOptionsItemSelected(item)
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