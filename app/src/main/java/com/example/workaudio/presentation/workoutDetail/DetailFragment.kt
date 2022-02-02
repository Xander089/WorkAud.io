package com.example.workaudio.presentation.workoutDetail

import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.workaudio.common.Constants
import com.example.workaudio.R
import com.example.workaudio.databinding.FragmentWorkoutDetailBinding
import com.example.workaudio.common.Constants.DURATION_TAG
import com.example.workaudio.common.Constants.ID_TAG
import com.example.workaudio.common.Constants.NAME_TAG
import com.example.workaudio.common.Constants.TAG
import com.example.workaudio.presentation.utils.dialogs.DialogFactory
import com.example.workaudio.presentation.utils.dialogs.DialogFlavour
import com.example.workaudio.presentation.player.PlayerActivity
import com.example.workaudio.presentation.utils.NavigationManager
import com.example.workaudio.presentation.utils.OnScrollListenerFactory
import com.example.workaudio.presentation.utils.itemTouchHelper.SwipeHelperCallback
import com.example.workaudio.presentation.utils.adapter.AdapterFactory
import com.example.workaudio.presentation.utils.adapter.AdapterFlavour
import com.example.workaudio.presentation.utils.adapter.DetailTracksAdapter
import com.example.workaudio.presentation.utils.modal.BottomModalDialog
import com.example.workaudio.presentation.utils.modal.ModalAction
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

    //onBackPressed -> navigate to home screen
    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                NavigationManager.navigateTo(
                    findNavController(),
                    DETAIL_TO_WORKOUTS
                )
            }
        }

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
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(onBackPressedCallback)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                NavigationManager.navigateTo(findNavController(), DETAIL_TO_WORKOUTS)
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
                viewModel.deleteTrack(uri)
            }
        ) as DetailTracksAdapter
        mItemTouchHelper = ItemTouchHelper(SwipeHelperCallback(workoutAdapter))
        mItemTouchHelper?.attachToRecyclerView(binding.trackList)
    }

    private fun addOnScrollListener() {
        binding.trackList.addOnScrollListener(
            OnScrollListenerFactory.create(
                onScrollStateChanged = { newState -> viewModel.scrollState = newState },
                onScrolled = { dy -> onScrolled(dy) }
            ))
    }

    private fun onScrolled(dy: Int) {
        if (dy > 0 && (viewModel.scrollState in listOf(0, 2))) {
            binding.topAppBar.visibility = View.GONE
        } else {
            binding.topAppBar.visibility = View.VISIBLE
        }
    }

    private fun showModalBottomFragment(trackUri: String) {
        val action: (String, Int) -> Unit = { uri, _ -> viewModel.deleteTrack(uri) }
        val bundle = bundleOf(
            Constants.MODAL_ACTION to ModalAction(trackUri, 0, action),
            Constants.MODAL_TITLE to getString(R.string.delete_selected_track)
        )
        val modalBottomSheet = BottomModalDialog()
        modalBottomSheet.arguments = bundle
        modalBottomSheet.show(parentFragmentManager, TAG)
    }


    private fun setLayoutFunctionality() {
        binding.apply {

            (activity as AppCompatActivity?)!!.setSupportActionBar(topAppBar)

            trackList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = workoutAdapter
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
        }
    }

    private fun setObservers() {
        viewModel.selectedWorkout.observe(this, { workout ->
            workout?.let {
                binding.apply {
                    topAppBar.title = it.name
                    targetDurationText.text = viewModel.formatTargetDuration(it.duration)
                }
            }
        })

        viewModel.tracks.observe(this, { tracks ->
            tracks?.let {
                togglePlayButton(viewModel.checkTracksDuration(it))
                binding.durationText.text = viewModel.getTracksDuration(it)
                workoutAdapter.refreshTrackList(it)
            }
        })

    }

    private fun togglePlayButton(enabled: Boolean) {
        binding.playButton.setOnClickListener {
            when (enabled) {
                true -> startPlayerActivity()
                false -> showSnackBar()
            }
        }
    }

    private fun showSnackBar() {
        val firstPart = getString(R.string.snackbar_duration_info)
        val secondPart = getString(R.string.snackbar_duration_info_second)
        val duration = viewModel.getTargetDurationText()
        val message = "$firstPart $duration $secondPart"
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_SHORT
        )
            .show()
    }

    private fun getColor(id: Int) = context?.resources?.getColor(id, null)

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

    private fun getWorkoutId() = arguments?.getInt(ID_TAG) ?: 0

}