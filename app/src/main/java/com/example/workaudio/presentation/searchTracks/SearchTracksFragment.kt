package com.example.workaudio.presentation.searchTracks

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.workaudio.R
import com.example.workaudio.databinding.FragmentEditingTracksBinding
import com.example.workaudio.Constants.ID_TAG
import com.example.workaudio.presentation.utils.OnScrollListenerFactory
import com.example.workaudio.presentation.utils.QueryTextListenerFactory
import com.example.workaudio.presentation.utils.adapter.AdapterFactory
import com.example.workaudio.presentation.utils.adapter.AdapterFlavour
import com.example.workaudio.presentation.utils.adapter.SearchedTracksAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchTracksFragment : Fragment() {


    @Inject
    lateinit var viewModel: SearchTracksFragmentViewModel

    private lateinit var trackListAdapter: SearchedTracksAdapter
    private lateinit var binding: FragmentEditingTracksBinding

    /* Lifecycle callbacks*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentEditingTracksBinding.inflate(inflater, container, false)

        viewModel.setupCurrentWorkout(getWorkoutId())
        buildAdapter()
        addOnScrollListener()
        setupLayout()
        setupObservers()


        return binding.root
    }

    //in order to provide app bar menu
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    //provides search view in app bar menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val searchView = provideSearchView(menu.findItem(R.id.action_search))
        val searchManager = provideSearchManager()
        setupSearchView(searchView, searchManager)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //when tapping the navigation icon in app bar, navigate to previous fragment
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController().popBackStack()
            }
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    private fun provideSearchView(menuItem: MenuItem): SearchView =
        menuItem.actionView as SearchView

    private fun provideSearchManager(): SearchManager =
        requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager

    //setup the search view query text listener
    private fun setupSearchView(searchView: SearchView, searchManager: SearchManager) {
        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
            val searchTracksQueryTextListener = QueryTextListenerFactory.create { newText ->
                viewModel.searchTracks(newText)
            }
            setOnQueryTextListener(searchTracksQueryTextListener)
        }
    }

    private fun showSoftKeyboard(view: View) {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
    private fun getResourceString(resId: Int) = requireActivity().resources.getString(resId)
    private fun getWorkoutId() = arguments?.getInt(ID_TAG) ?: 0

    private fun buildAdapter() {
        trackListAdapter = AdapterFactory.create(
            AdapterFlavour.SEARCHED,
            addTrack = { track ->
                viewModel.addTrack(track, getWorkoutId())
                viewModel.updateWorkoutDefaultImage(track.imageUrl, getWorkoutId())
                trackListAdapter.removeAddedTrack(track)
                showAddedTrackSnackBar(track.title)
            },
            fetchImage = { imageView, imageUri ->
                Glide.with(requireActivity()).load(imageUri).into(imageView)
            }
        ) as SearchedTracksAdapter
    }

    //when user scrolls up, hide the app bar
    private fun addOnScrollListener() {
        binding.trackList.addOnScrollListener(OnScrollListenerFactory.create(
            onScrollStateChanged = {dy -> onScrolledStateChanged(dy)},
            onScrolled = {newState -> viewModel.scrollState = newState}
        ))
    }

    private fun onScrolledStateChanged(dy: Int){
        if (dy > 0 && (viewModel.scrollState in listOf(0, 2))) {
            binding.topAppBar.visibility = View.GONE
        } else {
            binding.topAppBar.visibility = View.VISIBLE
        }

    }

    //when user add a tracks, then show snackbar with title track
    private fun showAddedTrackSnackBar(titleTrack: String) = Snackbar.make(
        binding.root,
        viewModel.formatSnackBarText(
            titleTrack,
            getResourceString(R.string.added_to_playlist)
        ),
        Snackbar.LENGTH_SHORT
    ).setBackgroundTint(resources.getColor(R.color.black_light2, null))
        .show()

    private fun setupLayout() {
        setTopAppBar()
        binding.trackList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = trackListAdapter
        }
    }

    private fun setTopAppBar() {
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.topAppBar)
    }

    private fun setupObservers() {
        setupSearchedTracksObserver()
        setupWorkoutTracksObserver()
        setupWorkoutObserver()
    }

    private fun setupWorkoutObserver() {
        viewModel.currentWorkout.observe(this, { workout ->
            binding.minuteLabel.text = viewModel.formatDuration(workout.duration)
            viewModel.setTargetDuration(workout)
        })
    }

    private fun setupWorkoutTracksObserver() {
        viewModel.workoutTracks.observe(this, { tracks ->
            binding.currentMinuteLabel.text = viewModel.formatCurrentDuration(tracks)
            binding.progressBar.progress = viewModel.updateProgressBar(tracks)
        })
    }

    private fun setupSearchedTracksObserver() {
        viewModel.searchedTracks.observe(this, { searchedTracks ->
            trackListAdapter.refreshTrackList(searchedTracks)
        })
    }

}