package com.example.workaudio.presentation.workoutMainList

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.core.widget.NestedScrollView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.workaudio.common.Constants.ID_TAG
import com.example.workaudio.common.Constants.MODAL_ACTION
import com.example.workaudio.common.Constants.MODAL_TITLE
import com.example.workaudio.R
import com.example.workaudio.common.Constants.GENRES
import com.example.workaudio.common.Constants.TAG
import com.example.workaudio.databinding.FragmentWorkoutList2Binding
import com.example.workaudio.presentation.utils.NavigationManager
import com.example.workaudio.presentation.utils.adapter.AdapterFactory
import com.example.workaudio.presentation.utils.adapter.AdapterFlavour
import com.example.workaudio.presentation.utils.adapter.GenreAdapter
import com.example.workaudio.presentation.utils.adapter.WorkoutAdapter
import com.example.workaudio.presentation.utils.modal.BottomModalDialog
import com.example.workaudio.presentation.utils.modal.ModalAction
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WorkoutListFragment : Fragment() {

    companion object {
        private const val WORKOUTS_TO_NAME = R.id.action_workoutListFragment_to_nameFragment
        private const val WORKOUTS_TO_DETAIL =
            R.id.action_workoutListFragment_to_workoutDetailFragment
    }

    private lateinit var binding: FragmentWorkoutList2Binding
    private lateinit var workoutAdapter: WorkoutAdapter
    private lateinit var genresAdapter: GenreAdapter

    @Inject
    lateinit var viewModel: WorkoutListFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkoutList2Binding.inflate(inflater, container, false)
        setupLayoutFunctionality()
        setupObservers()
        return binding.root
    }

    private fun setupLayoutFunctionality() {

        workoutAdapter = provideAdapter() as WorkoutAdapter
        genresAdapter = provideGenreAdapter() as GenreAdapter

        binding.apply {
            workoutList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = workoutAdapter
            }

            createWorkoutButton.setOnClickListener {
                NavigationManager.navigateTo(findNavController(), WORKOUTS_TO_NAME)
            }
            genresList.apply {
                layoutManager = GridLayoutManager(requireContext(), 2)
                adapter = genresAdapter
            }
            scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
                handleScroll(scrollY)
            })
            genresAdapter.update(GENRES, getDpiScreenWidth())
        }
    }

    private fun handleScroll(scrollPosition: Int) {
//        binding.mainTitle.text = when (scrollPosition) {
//            in 0..binding.anotherTextView.top -> binding.titleTextView.text.toString()
//            in binding.titleTextView.top..binding.materialButton.top -> binding.anotherTextView.text.toString()
//            else -> ""
//        }
    }

    private fun setupObservers() {
        viewModel.workouts.observe(this, { workouts ->
            workouts?.let {
                workoutAdapter.updateWorkouts(it)
            }
        })
    }

    private fun provideAdapter() = AdapterFactory.create(
        AdapterFlavour.WORKOUT,
        openWorkoutDetail = { workoutId ->
            val bundle = bundleOf(ID_TAG to workoutId)
            NavigationManager.navigateTo(findNavController(), WORKOUTS_TO_DETAIL, bundle)
        },
        showBottomModal = { workoutId ->
            showModalBottomFragment(workoutId)
        },
        fetchImage = { imageView, imageUri ->
            fetchImage(imageView, imageUri)
        }
    )

    private fun provideGenreAdapter() = AdapterFactory.create(
        AdapterFlavour.GENRE,
        open = {}
    )

    private fun fetchImage(imageView: ImageView, imageUri: String) {
        if (imageUri.isEmpty()) {
            return
        }
        Glide.with(requireActivity()).load(imageUri).into(imageView)
    }


    private fun showModalBottomFragment(workoutId: Int) {
        val action: (String, Int) -> Unit = { _, id -> viewModel.deleteWorkout(id) }
        val bundle = bundleOf(
            MODAL_ACTION to ModalAction("", workoutId, action),
            MODAL_TITLE to getString(R.string.delete_selected_workout)
        )
        val modalBottomSheet = BottomModalDialog()
        modalBottomSheet.arguments = bundle
        modalBottomSheet.show(parentFragmentManager, TAG)
    }

    private fun metrics(): DisplayMetrics {
        val outMetrics = DisplayMetrics()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val display = requireActivity().display
            display?.getRealMetrics(outMetrics)
        } else {
            @Suppress("DEPRECATION")
            val display = requireActivity().windowManager.defaultDisplay
            @Suppress("DEPRECATION")
            display.getMetrics(outMetrics)
        }
        return outMetrics
    }

    private fun getDpiScreenWidth() =
        metrics().widthPixels


}