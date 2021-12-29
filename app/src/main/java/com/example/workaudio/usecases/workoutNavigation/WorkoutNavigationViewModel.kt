package com.example.workaudio.usecases.workoutNavigation

import android.util.Log
import androidx.lifecycle.*
import com.example.workaudio.entities.Workout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutNavigationViewModel @Inject constructor(private val workoutNavigation: WorkoutNavigation) :
    ViewModel() {


    val workouts: LiveData<List<Workout>> = workoutNavigation.workouts.asLiveData()

}