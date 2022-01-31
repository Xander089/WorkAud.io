package com.example.workaudio.fragments.factory


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.example.workaudio.presentation.searchTracks.SearchTracksFragment
import com.example.workaudio.presentation.workoutCreation.DurationFragment
import com.example.workaudio.presentation.workoutCreation.NameFragment
import com.example.workaudio.presentation.workoutDetail.DetailFragment
import com.example.workaudio.presentation.workoutMainList.WorkoutListFragment
import javax.inject.Inject

class ConcreteFragmentFactory @Inject constructor() : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {

            NameFragment::class.java.name -> {
                NameFragment()
            }
            WorkoutListFragment::class.java.name -> {
                WorkoutListFragment()
            }
            DetailFragment::class.java.name -> {
                DetailFragment()
            }
            DurationFragment::class.java.name -> {
                DurationFragment()
            }
            SearchTracksFragment::class.java.name -> {
                SearchTracksFragment()
            }
            else -> super.instantiate(classLoader, className)
        }
    }
}