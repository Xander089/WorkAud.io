package com.example.workaudio.presentation.workoutDetail

import android.util.Log
import androidx.lifecycle.*
import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.core.usecases.detail.DetailBoundary
import com.example.workaudio.Constants.MILLIS_IN_A_MINUTE
import com.example.workaudio.Constants.MIN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailFragmentViewModel @Inject constructor(private val useCaseInteractor: DetailBoundary) :
    ViewModel() {

    private var dispatcher: CoroutineDispatcher = Dispatchers.IO
    fun setDispatcher(dispatcher: CoroutineDispatcher){
        this.dispatcher = dispatcher
    }

    var scrollState = 0

    var selectedWorkout: LiveData<Workout> = MutableLiveData<Workout>()
    var tracks: LiveData<List<Track>> = MutableLiveData()

    fun initializeCurrentWorkout(workoutId: Int) {
        selectedWorkout = useCaseInteractor.getWorkout(workoutId).asLiveData()
        tracks = useCaseInteractor.getWorkoutTracks(workoutId).asLiveData()
    }

    fun deleteTrack(trackUri: String) {
        val workoutId = selectedWorkout.value?.id ?: -1
        viewModelScope.launch(dispatcher) {
            useCaseInteractor.deleteTrack(trackUri, workoutId)
        }
    }

    fun updateWorkoutName(workoutId: Int, name: String) {
        viewModelScope.launch(dispatcher) {
            useCaseInteractor.updateWorkoutName(workoutId, name)
        }
    }

    fun updateWorkoutDuration(workoutId: Int, duration: Int) {
        viewModelScope.launch(dispatcher) {
            val durationInMillis = duration * MILLIS_IN_A_MINUTE
            useCaseInteractor.updateWorkoutDuration(workoutId, durationInMillis)
        }
    }

    fun durationToMinutes(duration: Int) = "${(duration / MILLIS_IN_A_MINUTE)}$MIN"

    fun getTracksDuration(tracks: List<Track>) =
        durationToMinutes(tracks.map { it.duration }.sum())

    fun tracksDurationCheck(tracks: List<Track>): Boolean {
        return tracks.map { it.duration }.sum() >= targetDuration()
    }

    private fun targetDuration() = selectedWorkout.value?.duration ?: 0


}