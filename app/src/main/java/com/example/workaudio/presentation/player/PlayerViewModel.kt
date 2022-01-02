package com.example.workaudio.presentation.player

import androidx.lifecycle.*
import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.core.usecases.player.PlayerInteractor
import com.example.workaudio.core.usecases.player.PlayerServiceBoundary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(private val playerInteractor: PlayerInteractor) :
    ViewModel() {

    val playerState = MutableLiveData<Int>(0)
    var selectedWorkout: LiveData<Workout> = MutableLiveData<Workout>()
    lateinit var countDownTimer: Flow<Int>
    private var _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> = _tracks
    val currentTrackPlaying = playerInteractor.getCurrentPosition().asLiveData()
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
                    val currentPlayerPosition = currentTrackPlaying.value?.position ?: 0
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
            playerInteractor.apply {
                val workout = getWorkout(workoutId)
                clearCurrentPosition()
                insertCurrentPosition(0)
                countDownTimer = buildCountDownTimer(workout.tracks)

                emit(workout)
            }
        }
    }

}