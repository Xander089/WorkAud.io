package com.example.workaudio.presentation.player

import android.util.Log
import androidx.lifecycle.*
import com.example.workaudio.DataHelper
import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.core.usecases.player.PlayerInteractor
import com.example.workaudio.core.usecases.player.PlayerServiceBoundary
import com.example.workaudio.presentation.utils.timer.TimerFactoryImpl
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

    //WORKOUT AND TRACKS
    val currentTrackPlaying = playerInteractor.getCurrentPosition().asLiveData()
    var selectedWorkout: LiveData<Workout> = MutableLiveData<Workout>()
    private var _tracks = MutableLiveData<List<PlayingTrack>>()
    val tracks: LiveData<List<PlayingTrack>> = _tracks

    //TIMER
    private val timerFactory = TimerFactoryImpl()

    //MAIN PLAYER TIMER
    private var countDownTimer: Flow<Int>? = null
    private lateinit var timerJob: Job
    private val _timerText = MutableLiveData<String>()
    val timerText: LiveData<String> = _timerText

    //CURRENT PLAYING SONG TIMER
    private var songTimer: Flow<Int>? = null
    private lateinit var songJob: Job
    private val _playingTrackText = MutableLiveData<String>()
    val playingTrackText: LiveData<String> = _playingTrackText


    fun startTimer() {
        playerState = PlayerState.PLAYING
        setTracksEndingTime()
        startMainTimerJob()
        startSongTimerJob()
    }

    private fun startMainTimerJob() {
        timerJob = viewModelScope.launch {
            playerInteractor.updateCurrentPosition(0)
            countDownTimer?.cancellable()?.collect { currentTime ->
                _timerText.value = DataHelper.toTime(currentTime)
                handlePlayer(currentTime, getCurrentPlayingSongEndingTime())
            }
        }
        viewModelScope.launch {
            timerJob.join()
        }
    }

    private fun startSongTimerJob() {
        songJob = viewModelScope.launch {
            songTimer?.cancellable()?.collect { currentTime ->
                _playingTrackText.value = DataHelper.formatTrackDuration(currentTime)
            }
        }
        viewModelScope.launch {
            songJob.join()
        }
    }

    fun restartTimer(time: String, songTime: String) {
        val seconds = DataHelper.fromTimeToSeconds(time)
        val songSeconds = DataHelper.fromMinutesToSeconds(songTime)
        countDownTimer = timerFactory.create(seconds, 1000L, false).get()
        songTimer = timerFactory.create(songSeconds, 1000L, true).get()
        restartMainTimerJob()
        startSongTimerJob()
    }

    private fun restartMainTimerJob() {
        timerJob = viewModelScope.launch {
            countDownTimer?.cancellable()?.collect { currentTime ->
                _timerText.value = DataHelper.toTime(currentTime)
                handlePlayer(currentTime, getCurrentPlayingSongEndingTime())
            }
        }
        viewModelScope.launch {
            timerJob.join()
        }
    }

    private fun getPlayerPosition() = currentTrackPlaying.value?.position ?: 0
    private fun getCurrentPlayingSongEndingTime() =
        tracks.value?.get(getPlayerPosition())?.endingTime ?: 0


    fun stopTimer() {
        timerJob.cancel()
        countDownTimer = null
        songJob.cancel()
        songTimer = null
    }

    private fun stopSongTimer() {
        songJob.cancel()
        songTimer = null
    }

    fun resetSongTimer(position: Int) {
        stopSongTimer()
        val nextSongDuration = (getTrack(position)?.duration ?: 0) / 1000
        songTimer = timerFactory.create(nextSongDuration, 1000L, true).get()
        startSongTimerJob()
    }

    private fun setTracksEndingTime() {
        var totTime = tracks.value?.map { track -> track.duration }?.sum() ?: 0
        tracks.value?.forEach { track ->
            track.endingTime = totTime - track.duration
            totTime -= track.duration
        }
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

    fun initializeWorkoutTracks(currentTracks: List<Track>) {
        _tracks.value = currentTracks.map {
            it.toPlayingTrack()
        }
        (currentTracks[0].duration / 1000).let { songDuration ->
            initializeSongTimer(songDuration)
        }
    }

    private fun initializeSongTimer(seconds: Int) {
        songTimer = timerFactory.create(seconds, 1000L, true).get()
    }

    fun initializeCurrentWorkout(workoutId: Int) {
        selectedWorkout = liveData(Dispatchers.IO) {
            delay(1000)
            playerInteractor.apply {
                val workout = getWorkout(workoutId)
                clearCurrentPosition()
                insertCurrentPosition(0)

                countDownTimer = timerFactory.create(
                    calculateTotalTracksTime(workout.tracks),
                    1000L,
                    false
                ).get()

                emit(workout)
            }
        }

    }

    fun getTrackUri(position: Int) = tracks.value?.get(position)?.uri.orEmpty()
    private fun getTrack(position: Int) = tracks.value?.get(position)
    private fun calculateTotalTracksTime(tracks: List<Track>) =
        tracks.map { it.duration / 1000 }.sum()

    fun initTimer(tracks: List<PlayingTrack>): String {
        val currentPlaylistTotalTime = tracks.map { it.duration }.sum() / 1000
        return DataHelper.toTime(currentPlaylistTotalTime)
    }

    fun formatTrackDuration(position: Int): String {
        val seconds = ((getTrack(position)?.duration) ?: 0) / 1000
        return DataHelper.formatTrackDuration(seconds)
    }

    fun getProgress(minutes: String): Int {
        val currentSeconds = DataHelper.fromMinutesToSeconds(minutes).toFloat()
        val currentPosition = currentTrackPlaying.value?.position ?: 0
        val totalSeconds = (((getTrack(currentPosition)?.duration) ?: 0) / 1000).toFloat()
        return (currentSeconds / totalSeconds * 100).toInt()
    }

    private fun Track.toPlayingTrack() = PlayingTrack(
        this.title,
        this.uri,
        this.duration,
        this.artist,
        this.album,
        this.imageUrl,
        this.endingTime
    )

}