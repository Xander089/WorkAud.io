package com.example.workaudio.presentation.searchTracks

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.workaudio.R
import com.example.workaudio.databinding.FragmentEditingTracksBinding
import com.example.workaudio.common.Constants.ID_TAG
import com.example.workaudio.core.entities.Track
import com.example.workaudio.presentation.utils.OnScrollListenerFactory
import com.example.workaudio.presentation.utils.QueryTextListenerFactory
import com.example.workaudio.presentation.utils.adapter.AdapterFactory
import com.example.workaudio.presentation.utils.adapter.AdapterFlavour
import com.example.workaudio.presentation.utils.adapter.SearchedTracksAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject

@AndroidEntryPoint
class SearchTracksFragment : Fragment() {

    @Inject
    lateinit var viewModel: SearchTracksFragmentViewModel

    private lateinit var trackListAdapter: SearchedTracksAdapter
    private lateinit var binding: FragmentEditingTracksBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentEditingTracksBinding.inflate(inflater, container, false)

        //to retrieve workout info and related tracks in live data observer
        viewModel.setupCurrentWorkout(getWorkoutId())

        buildAdapter()
        addOnScrollListener()
        setupLayout()
        setupObservers()

        return binding.root
    }

    //in order to inflate menu in app bar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun getResourceString(resId: Int) = context?.resources?.getString(resId).orEmpty()
    private fun getWorkoutId() = arguments?.getInt(ID_TAG) ?: 0

    //setup search view in app bar menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val searchView = menu
            .findItem(R.id.action_search)
            .actionView as SearchView

        val searchManager = requireActivity()
            .getSystemService(Context.SEARCH_SERVICE) as SearchManager
        setupSearchView(searchView, searchManager)
        super.onCreateOptionsMenu(menu, inflater)
    }


    //set the search view query text listener: when user is typing -> fetch tracks from spotify web API
    private fun setupSearchView(searchView: SearchView, searchManager: SearchManager) {
        setSearchViewHintTextColor(searchView)
        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
            setOnQueryTextListener(QueryTextListenerFactory.create { newText ->
                viewModel.searchTracksAsObservable(newText) { tracks ->
                    trackListAdapter.refreshTrackList(tracks)
                }
            })
        }
    }

    private fun setSearchViewHintTextColor(searchView: SearchView) {
        val searchViewEditTextId = searchView
            .context.resources.getIdentifier(
                getString(R.string.searchview_src),
                null,
                null
            )
        searchView.apply {
            queryHint = context.getString(R.string.standard_search)
            val textView: TextView = this.findViewById(searchViewEditTextId)
            textView.setHintTextColor(context.getColor(R.color.grey2))
        }
    }

    //when user taps navigation icon in app bar, navigate to previous fragment
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController().popBackStack()
            }
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }


    //create the recycler view adapter
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
            onScrollStateChanged = { newState -> viewModel.setScrollState(newState) },
            onScrolled = { dy -> onScrolled(dy) }
        ))
    }

    private fun onScrolled(dy: Int) {
        if (dy > 0 && (viewModel.getScrollState() in listOf(0, 2))) {
            binding.topAppBar.visibility = View.GONE
        } else {
            binding.topAppBar.visibility = View.VISIBLE
        }

    }

    private fun setupLayout() {
        //provides the app bar in fragment
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.topAppBar)

        binding.trackList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = trackListAdapter
        }
    }

    private fun setupObservers() {
        setupWorkoutTracksObserver()
        setupWorkoutObserver()
    }

    private fun setupWorkoutObserver() {
        viewModel.currentWorkout.observe(this, { workout ->
            workout?.let {
                binding.minuteLabel.text = viewModel.formatDuration(it.duration)
                viewModel.setTargetDuration(it)
            }
        })
    }

    private fun setupWorkoutTracksObserver() {
        viewModel.workoutTracks.observe(this, { tracks ->
            tracks?.let {
                binding.currentMinuteLabel.text = viewModel.formatCurrentDuration(it)
                binding.progressBar.progress = viewModel.updateProgressBar(it)
            }
        })
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

}