package com.example.workaudio.presentation.player

import android.util.Log
import androidx.lifecycle.*
import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.core.usecases.player.PlayerInteractor
import com.example.workaudio.core.usecases.player.PlayerServiceBoundary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(private val playerInteractor: PlayerServiceBoundary) :
    ViewModel() {



    //PLAYER STATE
    private var playerState = PlayerState.PAUSED
    fun getPlayerState() = playerState
    fun setPlayerState(state: PlayerState) {
        playerState = state
    }

    //TIMER
    lateinit var countDownTimer: Flow<Int>
    lateinit var timerJob: Job
    private val _timerText = MutableLiveData<String>()
    val timerText: LiveData<String> = _timerText

    //WORKOUT AND ITS TRACK
    val currentTrackPlaying = playerInteractor.getCurrentPosition().asLiveData()
    var selectedWorkout: LiveData<Workout> = MutableLiveData<Workout>()
    private var _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> = _tracks
    fun getTrackUri(position: Int) = tracks.value?.get(position)?.uri.orEmpty()
    fun initTimer(tracks: List<Track>): String {
        val currentPlaylistTotalTime = tracks.map { it.duration }.sum() / 1000
        return playerInteractor.toTime(currentPlaylistTotalTime)
    }

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
        playerState = PlayerState.PLAYING
        setTracksEndingTime()
        timerJob = viewModelScope.launch {
            playerInteractor.apply {
                updateCurrentPosition(0)
                countDownTimer.cancellable().collect { currentTime ->
                    _timerText.value = toTime(currentTime)
                    val currentPlayerPosition = currentTrackPlaying.value?.position ?: 0
                    val currentSongEndingTime = tracks.value
                        ?.get(currentPlayerPosition)
                        ?.endingTime ?: 0
                    handlePlayer(currentTime, currentSongEndingTime)
                }
            }
        }
        viewModelScope.launch {
            timerJob.join()
        }
    }

    fun restartTimer(time: String) {
        countDownTimer = playerInteractor.buildCountDownTimer(time)
        timerJob = viewModelScope.launch {
            playerInteractor.apply {
                countDownTimer.cancellable().collect { currentTime ->
                    _timerText.value = toTime(currentTime)
                    val currentPlayerPosition = currentTrackPlaying.value?.position ?: 0
                    val currentSongEndingTime = tracks.value
                        ?.get(currentPlayerPosition)
                        ?.endingTime ?: 0
                    handlePlayer(currentTime, currentSongEndingTime)
                }
            }
        }
        viewModelScope.launch {
            timerJob.join()
        }
    }

    fun stopTimer() {
        timerJob.cancel()
    }

    fun initializeWorkoutTracks(currentTracks: List<Track>) {
        _tracks.value = currentTracks
    }

    fun initializeCurrentWorkout(workoutId: Int) {
        selectedWorkout = liveData(Dispatchers.IO) {
            delay(1000)
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