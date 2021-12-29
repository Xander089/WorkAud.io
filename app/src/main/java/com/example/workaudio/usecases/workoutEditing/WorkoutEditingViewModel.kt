package com.example.workaudio.usecases.workoutEditing

import android.util.Log
import androidx.lifecycle.*
import com.example.workaudio.entities.Track
import com.example.workaudio.entities.Workout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutEditingViewModel @Inject constructor(private val workoutEditing: WorkoutEditing) :
    ViewModel() {


    var selectedWorkout: LiveData<Workout> = MutableLiveData<Workout>()
    var tracks: LiveData<List<Track>> = MutableLiveData<List<Track>>()

    fun setSelectedWorkout(workoutId: Int) {
        selectedWorkout = liveData(Dispatchers.IO) {
            val workout = workoutEditing.getWorkout(workoutId)
            emit(workout)
        }
        tracks = liveData(Dispatchers.IO) {
            val workout = workoutEditing.getWorkout(workoutId)
            emit(workout.tracks)
        }
    }

    fun updateWorkoutName(id: Int, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            workoutEditing.updateWorkoutName(id, name)
        }
    }


    fun updateWorkoutDuration(id: Int, duration: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            workoutEditing.updateWorkoutDuration(id, duration)
        }
    }

    fun insertWorkoutTrack(id: Int, track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            workoutEditing.insertWorkoutTrack(id, track)
        }
    }

    fun deleteTrack(uri: String, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            workoutEditing.deleteTrack(uri, id)
        }
    }
}