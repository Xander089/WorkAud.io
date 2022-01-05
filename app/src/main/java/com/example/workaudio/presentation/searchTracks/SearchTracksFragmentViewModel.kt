package com.example.workaudio.presentation.searchTracks

import android.util.Log
import androidx.lifecycle.*
import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.core.usecases.searchTracks.SearchInteractor
import com.example.workaudio.core.usecases.searchTracks.SearchServiceBoundary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchTracksFragmentViewModel @Inject constructor(private val useCaseInteractor: SearchInteractor) :
    ViewModel() {


    companion object {
        private const val MILLISECONDS_IN_A_MINUTE = 60000
        private const val MIN = " min"
    }

    private val searchInteractor: SearchServiceBoundary


    private val _searchedTracks = MutableLiveData<List<Track>>()
    val searchedTracks: LiveData<List<Track>> = _searchedTracks
    private var targetDuration = -1
    var currentWorkout: LiveData<Workout> = MutableLiveData<Workout>()
    var workoutTracks: LiveData<List<Track>> = MutableLiveData()

    fun setupCurrentWorkout(workoutId: Int) {
        currentWorkout = searchInteractor.getWorkout(workoutId).asLiveData()
        workoutTracks = searchInteractor.getWorkoutTracks(workoutId).asLiveData()
    }

    init {
        searchInteractor = useCaseInteractor
    }

    fun setTargetDuration(workout: Workout) {
        targetDuration = workout.duration
    }

    fun addTrack(track: Track, workoutId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            searchInteractor.addTrack(track, workoutId)
        }
    }

    fun searchTracks(queryText: String) {
        viewModelScope.launch(Dispatchers.Main) {
            if (queryText.isNotEmpty()) {
                _searchedTracks.value = searchInteractor.searchTracks(queryText)
            } else {
                _searchedTracks.value = emptyList()
            }
        }
    }

    fun updateWorkoutDefaultImage(imageUrl: String, workoutId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            searchInteractor.updateWorkoutDefaultImage(imageUrl, workoutId)
        }
    }

    fun updateProgressBar(tracks: List<Track>): Int {
        val target = targetDuration.toFloat()
        val current = tracks.map { it.duration }.sum().toFloat()
        Log.v("search__t", target.toString())
        Log.v("search__c", current.toString())
        return (current / target * 100).toInt()
    }

    fun formatCurrentDuration(tracks: List<Track>): String =
        tracks.map { it.duration / MILLISECONDS_IN_A_MINUTE }.sum().toString()

    fun formatDuration(duration: Int): String =
        "/${(duration / MILLISECONDS_IN_A_MINUTE)}$MIN"

    /*    fun compareDuration(): Boolean {
        val current = currentDuration.value ?: 0
        val workoutDuration = workoutDuration.value ?: 0
        return current > workoutDuration
    }*/

}