package com.example.workaudio.usecases.workoutCreation

import android.util.Log
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
class WorkoutCreationViewModel @Inject constructor(private val workoutCreation: WorkoutCreation) :
    ViewModel() {

    companion object {
        private const val MILLISECONDS_IN_A_MINUTE = 60000
    }

    private val workoutName = MutableLiveData<String>()
    private val workoutDuration = MutableLiveData<Int>()

    fun storeWorkoutInfo(name: String, duration: Int) {
        workoutName.value = name
        workoutDuration.value = duration * MILLISECONDS_IN_A_MINUTE
    }

    private val _currentDuration = MutableLiveData<Int>()
    val currentDuration: LiveData<Int> = _currentDuration

    init {
        _currentDuration.value = 0
    }

    private val _searchedTracks = MutableLiveData<List<Track>>()
    val searchedTracks: LiveData<List<Track>> = _searchedTracks

    private val _addedTracks = MutableLiveData<MutableList<Track>>()
    val addedTracks: LiveData<MutableList<Track>> = _addedTracks

    fun addTrack(track: Track) {
        val currentTracks = _addedTracks.value.orEmpty().toMutableList()
        if (_addedTracks.value.isNullOrEmpty()) {
            _addedTracks.value = mutableListOf(track)
        } else {
            currentTracks.add(track)
            _addedTracks.value = currentTracks
        }
        updateCurrentDuration(track.duration)
    }

    private fun updateCurrentDuration(duration: Int) {
        _currentDuration.value = _currentDuration.value?.plus(duration)
    }

    fun compareDuration(): Boolean {
        val current = currentDuration.value ?: 0
        val workoutDuration = workoutDuration.value ?: 0
        return current > workoutDuration
    }

    fun searchTracks(queryText: String) {
        viewModelScope.launch(Dispatchers.Main) {
            if (queryText.isNotEmpty()) {
                _searchedTracks.value = workoutCreation.searchTracks(queryText)
            } else {
                _searchedTracks.value = emptyList()
            }
        }
    }


    fun createWorkout() {
        viewModelScope.launch(Dispatchers.IO) {
            val name = workoutName.value.orEmpty()
            val duration = workoutDuration.value ?: 0
            val tracks = addedTracks.value.orEmpty().toList()
            workoutCreation.createWorkout(name, duration, tracks)
        }
    }


}