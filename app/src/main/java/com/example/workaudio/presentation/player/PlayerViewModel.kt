package com.example.workaudio.presentation.player

import androidx.lifecycle.*
import androidx.room.Update
import com.example.workaudio.Constants.DEFAULT_DELAY_TIME
import com.example.workaudio.Constants.MILLIS_TO_SECOND
import com.example.workaudio.DataHelper
import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.core.usecases.player.PlayerBoundary
import com.example.workaudio.presentation.utils.timer.AbstractTimerFactory
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
class PlayerViewModel @Inject constructor(private val playerInteractor: PlayerBoundary) :
    ViewModel() {


    //PLAYER STATE
    private var playerState = PlayerState.PAUSED
    fun getPlayerState() = playerState
    fun setPlayerState(state: PlayerState) {
        playerState = state
    }

    //Workout & Player Position
    var workout: LiveData<Workout> = MutableLiveData<Workout>()
    private var _playerPosition = MutableLiveData<Int>(0)
    var playerPosition: LiveData<Int> = _playerPosition
    private fun getPlayerPosition() = _playerPosition.value ?: 0

    @Inject
    lateinit var timerFactory: AbstractTimerFactory

    //MAIN PLAYER TIMER
    private var countDownTimer: Flow<Int>? = null
    private lateinit var timerJob: Job
    private val _timerText = MutableLiveData<String>()
    val timerText: LiveData<String> = _timerText

    //CURRENT PLAYING SONG TIMER
    private var songTimer: Flow<Int>? = null
    private var songJob: Job? = null
    private val _playingTrackText = MutableLiveData<String>()
    val songTimerText: LiveData<String> = _playingTrackText


    fun emitWorkout(workoutId: Int) {
        workout = liveData(Dispatchers.IO) {
            delay(DEFAULT_DELAY_TIME)
            val workout = playerInteractor.getWorkout(workoutId)
            val totalDuration = calculateTotalTracksTime(workout.tracks)
            val firstTrackDuration = getTrackDuration(0).toInt()
            songTimer = createTimer(firstTrackDuration, true)
            countDownTimer = createTimer(totalDuration, false)
            emit(workout)
        }
    }

    private fun createTimer(seconds: Int, ascending: Boolean): Flow<Int> =
        timerFactory.create(seconds, DEFAULT_DELAY_TIME, ascending).get()

    fun initTimer() {
        playerState = PlayerState.PLAYING
        setTracksEndingTime()
        startMainTimerJob()
        _playerPosition.value = 0

    }

    private fun createJob(flow: Flow<Int>?, update: (Int) -> Unit): Job = viewModelScope.launch {
        flow?.cancellable()?.collect { currentTime ->
            update(currentTime)
        }
    }

    private fun Job.launch() {
        viewModelScope.launch {
            this@launch.join()
        }
    }


    private fun startSongTimerJob() {
        songJob = createJob(songTimer) { currentTime ->
            _playingTrackText.value = DataHelper.formatTrackDuration(currentTime)
        }.also {
            it.launch()
        }

    }

    private fun startMainTimerJob() {
        timerJob = createJob(countDownTimer) { currentTime ->
            _timerText.value = DataHelper.toTime(currentTime)
            setNextSong(currentTime, getCurrentPlayingSongEndingTime())
        }.also {
            it.launch()
        }
    }

    private fun setNextSong(currentTime: Int, currentSongEndingTime: Int) {
        if (currentTime <= 0) {
            return
        } else if (currentTime <= currentSongEndingTime * MILLIS_TO_SECOND) {
            _playerPosition.value = getPlayerPosition() + 1
        }
    }


    fun restartTimer(time: String, songTime: String) {
        val seconds = DataHelper.fromTimeToSeconds(time)
        val songSeconds = DataHelper.fromMinutesToSeconds(songTime)
        countDownTimer = createTimer(seconds, false)
        songTimer = createTimer(songSeconds, true)
        startMainTimerJob()
        startSongTimerJob()
    }


    fun stopTimer() {
        timerJob.cancel()
        countDownTimer = null
        songJob?.cancel()
        songTimer = null
    }

    private fun disposeSongTimer() {
        songJob?.cancel()
        songTimer = null
    }

    fun setSongTimer(position: Int) {
        disposeSongTimer()
        val nextSongDuration = getTrackDuration(position).toInt()
        songTimer = createTimer(nextSongDuration, true)
        startSongTimerJob()
    }

    private fun setTracksEndingTime() {
        getTracks().let { tracks ->
            var totTime = tracks.map { track -> track.duration }.sum()
            tracks.forEach { track ->
                track.endingTime = totTime - track.duration
                totTime -= track.duration
            }
        }
    }

    private fun getCurrentPlayingSongEndingTime() = getTracks()[getPlayerPosition()].endingTime
    private fun getTracks() = workout.value?.tracks.orEmpty()
    private fun getTrack(position: Int) = workout.value?.tracks?.get(position)
    fun getTrackUri(position: Int) = getTrack(position)?.uri.orEmpty()
    fun getTrackName(position: Int) = getTrack(position)?.title.orEmpty()
    fun getTrackArtist(position: Int) = getTrack(position)?.artist.orEmpty()
    private fun calculateTotalTracksTime(tracks: List<Track>) =
        tracks.map { it.duration * MILLIS_TO_SECOND }.sum().toInt()

    private fun getTrackDuration(position: Int) =
        (((getTrack(position)?.duration) ?: 0) * MILLIS_TO_SECOND)

    fun formatTimer(tracks: List<Track>): String {
        val totTime = (tracks.map { it.duration }.sum() * MILLIS_TO_SECOND).toInt()
        return DataHelper.toTime(totTime)
    }

    fun formatTrackDuration(position: Int): String {
        val seconds = getTrackDuration(position).toInt()
        return DataHelper.formatTrackDuration(seconds)
    }

    fun getProgress(minutes: String): Int {
        val currentSeconds = DataHelper.fromMinutesToSeconds(minutes).toFloat()
        val totalSeconds = getTrackDuration(getPlayerPosition())
        return (currentSeconds / totalSeconds * 100).toInt()
    }

}