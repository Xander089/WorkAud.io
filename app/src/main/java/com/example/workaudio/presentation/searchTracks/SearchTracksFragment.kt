package com.example.workaudio.presentation.searchTracks

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
import com.example.workaudio.databinding.FragmentEditingTracksBinding
import com.example.workaudio.presentation.Constants.ID_TAG
import com.google.android.material.snackbar.Snackbar


class SearchTracksFragment : Fragment() {


    private val viewModel: SearchTracksFragmentViewModel by activityViewModels()
    private lateinit var trackListAdapter: SearchedTracksAdapter
    private lateinit var binding: FragmentEditingTracksBinding

    private val searchTracksQueryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextChange(newText: String): Boolean {
            viewModel.searchTracks(newText)
            return true
        }

        override fun onQueryTextSubmit(query: String) = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentEditingTracksBinding.inflate(inflater, container, false)

        viewModel.setupCurrentWorkout(getWorkoutId())
        buildAdapter()
        setupLayout()
        setupObservers()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val searchView = provideSearchView(menu.findItem(R.id.action_search))
        val searchManager = provideSearchManager()
        setupSearchView(searchView, searchManager)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController().popBackStack()
            }
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getWorkoutId() = arguments?.getInt(ID_TAG) ?: 0

    private fun buildAdapter() {
        trackListAdapter = SearchedTracksAdapter.newInstance(
            addTrack = { track ->
                viewModel.addTrack(track, getWorkoutId())
                viewModel.updateWorkoutDefaultImage(track.imageUrl, getWorkoutId())
                trackListAdapter.removeAddedTrack(track)
                showAddedTrackSnackBar(track.title)
            },
            fetchImage = { imageView, imageUri ->
                Glide.with(requireActivity()).load(imageUri).into(imageView)
            }
        )
    }

    private fun showAddedTrackSnackBar(titleTrack: String) = Snackbar.make(
        binding.root,
        viewModel.formatSnackBarText(
            titleTrack,
            getResourceString(R.string.added_to_playlist)),
        Snackbar.LENGTH_SHORT
    ).setBackgroundTint(resources.getColor(R.color.black_light2, null))
        .show()


    private fun getResourceString(resId: Int) = requireActivity().resources.getString(resId)


    private fun setupLayout() {
        binding.apply {
            (activity as AppCompatActivity?)!!.setSupportActionBar(topAppBar)
            trackList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = trackListAdapter
            }
        }
    }

    private fun setupObservers() {
        viewModel.searchedTracks.observe(this, { searchedTracks ->
            trackListAdapter.refreshTrackList(searchedTracks)
        })

        viewModel.workoutTracks.observe(this, { tracks ->
            binding.currentMinuteLabel.text = viewModel.formatCurrentDuration(tracks)
            binding.progressBar.progress = viewModel.updateProgressBar(tracks)
        })

        viewModel.currentWorkout.observe(this, { workout ->
            binding.minuteLabel.text = viewModel.formatDuration(workout.duration)
            viewModel.setTargetDuration(workout)
        })
    }


    private fun provideSearchView(menuItem: MenuItem): SearchView =
        menuItem.actionView as SearchView

    private fun provideSearchManager(): SearchManager =
        requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager

    private fun setupSearchView(searchView: SearchView, searchManager: SearchManager) {
        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
            setOnQueryTextListener(searchTracksQueryTextListener)
        }
    }

}