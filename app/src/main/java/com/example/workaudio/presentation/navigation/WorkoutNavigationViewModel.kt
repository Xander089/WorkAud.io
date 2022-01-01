package com.example.workaudio.presentation.navigation

import androidx.lifecycle.*
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.core.usecases.navigation.WorkoutNavigationInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WorkoutNavigationViewModel @Inject constructor(private val workoutNavigationInteractor: WorkoutNavigationInteractor) :
    ViewModel() {

    val workouts: LiveData<List<Workout>> = workoutNavigationInteractor.workouts.asLiveData()

}