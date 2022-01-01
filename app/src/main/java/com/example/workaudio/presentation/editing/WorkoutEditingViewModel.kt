package com.example.workaudio.presentation.editing

import android.util.Log
import androidx.lifecycle.*
import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.core.usecases.editing.WorkoutEditingInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutEditingViewModel @Inject constructor(private val workoutEditingInteractor: WorkoutEditingInteractor) :
    ViewModel() {

    var selectedWorkout: LiveData<Workout> = MutableLiveData<Workout>()
    var tracks: LiveData<List<Track>> = MutableLiveData<List<Track>>()

    fun initializeCurrentWorkout(workoutId: Int) {
        selectedWorkout = liveData(Dispatchers.IO) {
            val workout = workoutEditingInteractor.getWorkout(workoutId)
            emit(workout)
        }
        tracks = liveData(Dispatchers.IO) {
            val workout = workoutEditingInteractor.getWorkout(workoutId)
            emit(workout.tracks)
        }
    }

    fun updateWorkoutName(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val workoutId = selectedWorkout.value?.id ?: 0
            workoutEditingInteractor.updateWorkoutName(workoutId, name)
        }
    }

    fun updateWorkoutDuration(duration: Int) {
        val workoutId = selectedWorkout.value?.id ?: -1
        viewModelScope.launch(Dispatchers.IO) {
            val durationInMillis = duration * 60 * 1000
            workoutEditingInteractor.updateWorkoutDuration(workoutId, durationInMillis)
        }
    }
}