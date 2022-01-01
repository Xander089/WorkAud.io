package com.example.workaudio.presentation.navigation

import androidx.lifecycle.*
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.core.usecases.navigation.WorkoutNavigationInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutNavigationViewModel @Inject constructor(private val workoutNavigationInteractor: WorkoutNavigationInteractor) :
    ViewModel() {

    val workouts: LiveData<List<Workout>> = workoutNavigationInteractor.workouts.asLiveData()

    fun deleteWorkout(workoutId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            workoutNavigationInteractor.deleteWorkout(workoutId)
        }
    }
}