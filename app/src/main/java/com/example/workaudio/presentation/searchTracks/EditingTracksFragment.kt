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
import com.example.workaudio.core.entities.Track
import com.example.workaudio.databinding.FragmentEditingTracksBinding


class EditingTracksFragment : Fragment() {

    companion object {
        private const val ID_TAG = "id"
    }

    private val viewModel: EditingTracksFragmentViewModel by activityViewModels()
    private lateinit var trackListAdapter: WorkoutTracksAdapter

    private lateinit var binding: FragmentEditingTracksBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentEditingTracksBinding.inflate(inflater, container, false)
        buildAdapter()
        setupLayout()
        setupObservers()

        return binding.root
    }

    private fun buildAdapter() {
        trackListAdapter = WorkoutTracksAdapter(
            mutableListOf<Track>(),
            addTrack = { track ->
                val workoutId = arguments?.getInt(ID_TAG) ?: 0
                viewModel.addTrack(track, workoutId)
            },
            fetchImage = { imageView, imageUri ->
                Glide.with(requireActivity()).load(imageUri).into(imageView)
            }
        )
    }

    private fun setupLayout() {
        binding.apply {
            (activity as AppCompatActivity?)!!.setSupportActionBar(topAppBar)

            //DA AGGIORNARE IN OBSERVERS
            //minuteLabel.text = "0/${(arguments?.getInt(TracksFragment.WORKOUT_DURATION) ?: 0)} min"

            trackList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = trackListAdapter
            }
        }
    }

    private fun setupObservers() {
        viewModel.searchedTracks.observe(this, { searchedTracks ->
            trackListAdapter.apply {
                tracks.clear()
                tracks.addAll(searchedTracks)
                notifyDataSetChanged()
            }
        })

        viewModel.currentDuration.observe(this, { duration ->
            binding.apply {
                minuteLabel.text = (duration / 60000).toString()
            }
        })
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
            android.R.id.home -> findNavController().popBackStack()
            else -> {}
        }

        return super.onOptionsItemSelected(item)
    }

}