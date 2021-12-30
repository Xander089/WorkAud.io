package com.example.workaudio.usecases.player

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.example.workaudio.entities.Track
import com.example.workaudio.entities.Workout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(private val playerInteractor: Player) :
    ViewModel() {

    val playerState = MutableLiveData<Int>(0)
    var selectedWorkout: LiveData<Workout> = MutableLiveData<Workout>()

    private var _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> = _tracks

    val currentTrackPosition = playerInteractor.getCurrentPosition().asLiveData()

    private val _timerText = MutableLiveData<String>()
    val timerText: LiveData<String> = _timerText

    private fun handlePlayer(currentTime: Int, currentSongEndingTime: Int) {
        if (currentTime <= 0) {
            return
        }
        if (currentTime <= currentSongEndingTime / 1000) {
            viewModelScope.launch(Dispatchers.IO) {
                playerInteractor.updateCurrentPosition(1)
            }
        }
    }

    private fun setTracksEndingTime() {
        var totTime = tracks.value?.map { track -> track.duration }?.sum() ?: 0
        tracks.value?.forEach { track ->
            track.endingTime = totTime - track.duration
            totTime -= track.duration
        }
    }

    fun startTimer() {
        playerState.value = 1
        setTracksEndingTime()
        viewModelScope.launch {
            playerInteractor.apply {
                updateCurrentPosition(0)
                countDownTimer.collect { currentTime ->
                    _timerText.value = toTime(currentTime)
                    val currentPlayerPosition = currentTrackPosition.value?.position ?: 0
                    val currentSongEndingTime = tracks.value
                        ?.get(currentPlayerPosition)
                        ?.endingTime ?: 0
                    handlePlayer(currentTime, currentSongEndingTime)
                }
            }
        }
    }

    fun initializeWorkoutTracks(currentTracks: List<Track>) {
        _tracks.value = currentTracks
    }

    fun getTrackUri(position: Int) = tracks.value?.get(position)?.uri.orEmpty()

    fun initializeCurrentWorkout(workoutId: Int) {
        selectedWorkout = liveData(Dispatchers.IO) {
            val workout = playerInteractor.getWorkout(workoutId)
            playerInteractor.clearCurrentPosition()
            playerInteractor.insertCurrentPosition(0)
            playerInteractor.apply {
                countDownTimer = buildCountDownTimer(workout.tracks)
            }
            emit(workout)
        }
    }

}