package com.example.workaudio.presentation.editing

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
    fun updateWorkoutName(id: Int, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            workoutEditingInteractor.updateWorkoutName(id, name)
        }
    }


    fun updateWorkoutDuration(id: Int, duration: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            workoutEditingInteractor.updateWorkoutDuration(id, duration)
        }
    }

    fun insertWorkoutTrack(id: Int, track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            workoutEditingInteractor.insertWorkoutTrack(id, track)
        }
    }

    fun deleteTrack(uri: String, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            workoutEditingInteractor.deleteTrack(uri, id)
        }
    }
}