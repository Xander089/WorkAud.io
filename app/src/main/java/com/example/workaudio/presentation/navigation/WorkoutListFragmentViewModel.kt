package com.example.workaudio.presentation.navigation

import androidx.lifecycle.*
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.core.usecases.navigation.NavigationServiceBoundary
import com.example.workaudio.core.usecases.navigation.NavigationInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutListFragmentViewModel @Inject constructor(private val workoutNavigationInteractor: NavigationServiceBoundary) :
    ViewModel() {

    var workouts: LiveData<List<Workout>> = workoutNavigationInteractor.getWorkouts().asLiveData()

    fun deleteWorkout(workoutId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            workoutNavigationInteractor.deleteWorkout(workoutId)
        }
    }
}