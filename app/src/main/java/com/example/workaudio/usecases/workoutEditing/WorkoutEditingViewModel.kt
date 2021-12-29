package com.example.workaudio.usecases.workoutEditing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workaudio.entities.Track
import com.example.workaudio.entities.Workout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutEditingViewModel @Inject constructor(private val workoutEditing: WorkoutEditing) :
    ViewModel() {

    private val _selectedWorkout = MutableLiveData<Workout>()
    private val selectedWorkout: LiveData<Workout> = _selectedWorkout

    fun getWorkout(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _selectedWorkout.value = workoutEditing.getWorkout(id)
        }
    }

    fun updateWorkoutName(id: Int, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            workoutEditing.updateWorkoutName(id, name)
        }
    }

    fun updateWorkoutCurrentDuration(id: Int, currentDuration: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            workoutEditing.updateWorkoutCurrentDuration(id, currentDuration)
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