package com.example.workaudio.presentation.workoutDetail

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.workaudio.R
import com.example.workaudio.databinding.FragmentWorkoutDetailBinding
import com.example.workaudio.Constants.DURATION_TAG
import com.example.workaudio.Constants.ID_TAG
import com.example.workaudio.Constants.NAME_TAG
import com.example.workaudio.Constants.TAG
import com.example.workaudio.presentation.utils.dialogs.DialogFactory
import com.example.workaudio.presentation.utils.dialogs.DialogFlavour
import com.example.workaudio.presentation.player.PlayerActivity
import com.example.workaudio.presentation.utils.itemTouchHelper.NavigationManager
import com.example.workaudio.presentation.utils.itemTouchHelper.SwipeHelperCallback
import com.example.workaudio.presentation.utils.adapter.AdapterFactory
import com.example.workaudio.presentation.utils.adapter.AdapterFlavour
import com.example.workaudio.presentation.utils.adapter.DetailTracksAdapter
import com.google.android.material.snackbar.Snackbar


class DetailFragment : Fragment() {

    companion object {
        private const val DETAIL_TO_EDITING_TRACKS =
            R.id.action_workoutDetailFragment_to_editingTracksFragment
        private const val DETAIL_TO_WORKOUTS =
            R.id.action_workoutDetailFragment_to_workoutListFragment
    }

    private lateinit var binding: FragmentWorkoutDetailBinding
    private lateinit var workoutAdapter: DetailTracksAdapter
    private var mItemTouchHelper: ItemTouchHelper? = null
    private val viewModel: DetailFragmentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentWorkoutDetailBinding.inflate(inflater, container, false)

        buildAdapter()
        addOnScrollListener()
        viewModel.initializeCurrentWorkout(getWorkoutId())
        setLayoutFunctionality()
        setObservers()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController().popBackStack()
            }
            R.id.action_edit -> {
                showEditNameDialogFragment()
            }
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    private fun buildAdapter() {
        workoutAdapter = AdapterFactory.create(
            AdapterFlavour.DETAIL,
            fetchImage = { imageView, imageUri ->
                Glide.with(requireActivity()).load(imageUri).into(imageView)
            },
            deleteTrack = { uri ->
                showModalBottomFragment(uri)
            },
            onSwipe = { uri ->
                showModalBottomFragment(uri)
            }
        ) as DetailTracksAdapter
        mItemTouchHelper = ItemTouchHelper(SwipeHelperCallback(workoutAdapter))
        mItemTouchHelper?.attachToRecyclerView(binding.trackList)
    }

    private fun addOnScrollListener() {
        binding.trackList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                viewModel.scrollState = newState
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0 && (viewModel.scrollState in listOf(0, 2))) {
                    binding.topAppBar.visibility = View.GONE
                } else {
                    binding.topAppBar.visibility = View.VISIBLE
                }
            }

        })
    }

    private fun showModalBottomFragment(trackUri: String) {
        BottomModalSelectTrack(trackUri) { uri ->
            viewModel.deleteTrack(uri)
        }
            .show(parentFragmentManager, TAG)
    }


    private fun setLayoutFunctionality() {
        binding.apply {

            (activity as AppCompatActivity?)!!.setSupportActionBar(topAppBar)

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

            durationText.setOnClickListener {
                showEditDurationDialogFragment()
            }
            targetDurationText.setOnClickListener {
                showEditDurationDialogFragment()
            }

            infoButton.setOnClickListener {
                Snackbar.make(
                    binding.root,
                    getString(R.string.snackbar_duration_info),
                    Snackbar.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    private fun setObservers() {
        viewModel.selectedWorkout.observe(this, { workout ->

            binding.apply {
                topAppBar.title = workout.name
                durationText.text = viewModel.durationToMinutes(workout.duration)
            }
        })

        viewModel.tracks.observe(this, { tracks ->
            togglePlayButton(viewModel.tracksDurationCheck(tracks))
            binding.durationText.text = viewModel.getTracksDuration(tracks)
            workoutAdapter.refreshTrackList(tracks)
        })

    }

    private fun togglePlayButton(enabled: Boolean) {
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
        DialogFactory.create(
            DialogFlavour.EDIT_NAME,
            updateWorkout = { name ->
                viewModel.updateWorkoutName(getWorkoutId(), name)
            })
            .show(parentFragmentManager, NAME_TAG)
    }

    private fun showEditDurationDialogFragment() {
        DialogFactory.create(
            DialogFlavour.EDIT_DURATION,
            updateDuration = { duration ->
            viewModel.updateWorkoutDuration(getWorkoutId(), duration)
        })
            .show(parentFragmentManager, DURATION_TAG)
    }

    private fun getWorkoutId() = arguments?.getInt(ID_TAG) ?: -1

}