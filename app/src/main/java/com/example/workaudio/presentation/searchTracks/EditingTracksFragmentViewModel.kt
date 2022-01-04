package com.example.workaudio.presentation.searchTracks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.usecases.searchTracks.SearchInteractor
import com.example.workaudio.core.usecases.searchTracks.SearchServiceBoundary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditingTracksFragmentViewModel @Inject constructor(private val useCaseInteractor: SearchInteractor) :
    ViewModel() {


    companion object {
        private const val MILLISECONDS_IN_A_MINUTE = 60000
    }

    private val searchInteractor: SearchServiceBoundary

    private val _currentDuration = MutableLiveData<Int>()
    val currentDuration: LiveData<Int> = _currentDuration

    private val _searchedTracks = MutableLiveData<List<Track>>()
    val searchedTracks: LiveData<List<Track>> = _searchedTracks


    init {
        _currentDuration.value = 0
        searchInteractor = useCaseInteractor
    }


    fun addTrack(track: Track, workoutId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            searchInteractor.addTrack(track, workoutId)
        }
        updateCurrentDuration(track.duration)
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

    private fun updateCurrentDuration(duration: Int) {
        _currentDuration.value = _currentDuration.value?.plus(duration)
    }

    /*    fun compareDuration(): Boolean {
        val current = currentDuration.value ?: 0
        val workoutDuration = workoutDuration.value ?: 0
        return current > workoutDuration
    }*/

}