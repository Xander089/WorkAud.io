package com.example.workaudio.usecases.player

import com.example.workaudio.entities.Track
import com.example.workaudio.entities.Workout
import com.example.workaudio.repository.database.CurrentPosition
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Player(
    private val facade: PlayerFacade
) {

    companion object {
        private const val RESET_TIME = "00:00:00"
    }

    lateinit var countDownTimer: Flow<Int>

    fun getCurrentPosition() = facade.getCurrentPosition()
    suspend fun clearCurrentPosition() = facade.clearCurrentPosition()
    suspend fun insertCurrentPosition(position: Int) =
        facade.insertCurrentPosition(CurrentPosition(position))

    suspend fun updateCurrentPosition(pos: Int) = facade.updateCurrentPosition(pos)

    suspend fun getWorkout(id: Int): Workout = facade.getWorkout(id)

    fun toTime(seconds: Int): String {

        if (seconds <= 0) {
            return RESET_TIME
        }

        val hour = seconds / 3600
        var remainderSeconds = seconds % 3600
        val minutes = remainderSeconds / 60
        remainderSeconds %= 60

        val h = if (hour < 10) "0$hour" else "$hour"
        val m = if (minutes < 10) "0$minutes" else "$minutes"
        val s = if (remainderSeconds < 10) "0$remainderSeconds" else "$remainderSeconds"

        return "$h:$m:$s"
    }

    fun buildCountDownTimer(tracks: List<Track>): Flow<Int> {
        val totTime = tracks.map { track -> track.duration }.sum()
        return flow<Int> {
            var currentTime = totTime / 1000
            while (currentTime >= 0) {
                delay(1000)
                currentTime--
                emit(currentTime)
            }
        }
    }
}