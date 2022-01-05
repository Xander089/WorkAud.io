package com.example.workaudio.presentation.editing

import android.util.Log
import androidx.lifecycle.*
import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.core.usecases.editing.EditingServiceBoundary
import com.example.workaudio.core.usecases.editing.WorkoutEditingInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutDetailFragmentViewModel @Inject constructor(private val _editingInteractor: WorkoutEditingInteractor) :
    ViewModel() {

    private val editingInteractor: EditingServiceBoundary

    init {
        editingInteractor = _editingInteractor
    }

    var selectedWorkout: LiveData<Workout> = MutableLiveData<Workout>()
    var tracks: LiveData<List<Track>> = MutableLiveData()

    fun initializeCurrentWorkout(workoutId: Int) {
        selectedWorkout = editingInteractor.getWorkout(workoutId).asLiveData()
        tracks = editingInteractor.getWorkoutTracks(workoutId).asLiveData()
    }

    fun deleteTrack(trackUri: String) {
        val workoutId = selectedWorkout.value?.id ?: -1
        viewModelScope.launch(Dispatchers.IO) {
            editingInteractor.deleteTrack(trackUri, workoutId)
        }
    }

    fun updateWorkoutName(workoutId: Int, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            editingInteractor.updateWorkoutName(workoutId, name)
        }
    }

    fun updateWorkoutDuration(workoutId: Int, duration: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val durationInMillis = duration * MILLISECONDS_IN_A_MINUTE
            editingInteractor.updateWorkoutDuration(workoutId, durationInMillis)
        }
    }

    fun durationToMinutes(duration: Int) = "${(duration / MILLISECONDS_IN_A_MINUTE)}$MIN"

    companion object {
        private const val MIN = " min"
        private const val MILLISECONDS_IN_A_MINUTE = 60000
    }
}