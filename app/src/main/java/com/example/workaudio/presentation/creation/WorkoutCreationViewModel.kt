package com.example.workaudio.presentation.creation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.usecases.creation.CreationServiceBoundary
import com.example.workaudio.core.usecases.creation.WorkoutCreationInteractor
import com.example.workaudio.core.usecases.player.PlayerServiceBoundary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutCreationViewModel @Inject constructor(private val _workoutCreationInteractor: WorkoutCreationInteractor) :
    ViewModel() {


    companion object {
        private const val MILLISECONDS_IN_A_MINUTE = 60000
    }

    //LIVEDATA
    private val workoutName = MutableLiveData<String>()
    private val workoutDuration = MutableLiveData<Int>()

    private val _currentDuration = MutableLiveData<Int>()
    val currentDuration: LiveData<Int> = _currentDuration

    private val _searchedTracks = MutableLiveData<List<Track>>()
    val searchedTracks: LiveData<List<Track>> = _searchedTracks

    private val _addedTracks = MutableLiveData<MutableList<Track>>()
    val addedTracks: LiveData<MutableList<Track>> = _addedTracks

    private val workoutCreationInteractor: CreationServiceBoundary

    init {
        _currentDuration.value = 0
        workoutCreationInteractor = _workoutCreationInteractor
    }

    fun cacheWorkoutInfo(name: String, duration: Int) {
        workoutName.value = name
        workoutDuration.value = duration * MILLISECONDS_IN_A_MINUTE
    }

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

    fun removeTrack(track: Track) {

        val currentTracks = _addedTracks.value.orEmpty().toMutableList()

        if (currentTracks.contains(track)) {
            currentTracks.remove(track)
            _addedTracks.value = currentTracks
            updateCurrentDuration(-track.duration)
        }
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
                _searchedTracks.value = workoutCreationInteractor.searchTracks(queryText)
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
            workoutCreationInteractor.createWorkout(name, duration, tracks)
        }
    }


}