package com.example.workaudio.usecases.workoutNavigation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workaudio.entities.Workout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutNavigationViewModel @Inject constructor(private val workoutNavigation: WorkoutNavigation) :
    ViewModel() {

    private val _workouts = MutableLiveData<List<Workout>>()
    private val workouts: LiveData<List<Workout>> = _workouts

    private val _selectedWorkout = MutableLiveData<Workout>()
    private val selectedWorkout: LiveData<Workout> = _selectedWorkout

    private fun getAllWorkouts() {
        viewModelScope.launch(Dispatchers.IO) {
            _workouts.value = workoutNavigation.getAllWorkouts()
        }
    }

    fun getWorkout(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _selectedWorkout.value = workoutNavigation.getWorkout(id)
        }
    }

    init {
        getAllWorkouts()
    }


}