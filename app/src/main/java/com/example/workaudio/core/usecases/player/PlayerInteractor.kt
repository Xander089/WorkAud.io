package com.example.workaudio.core.usecases.player

import android.util.Log
import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.repository.database.CurrentPosition
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlayerInteractor(
    override val facade: PlayerFacade
) : PlayerServiceBoundary(facade) {

    companion object {
        private const val RESET_TIME = "00:00:00"
    }

    override fun getCurrentPosition() = facade.getCurrentPosition()
    override suspend fun clearCurrentPosition() = facade.clearCurrentPosition()
    override suspend fun insertCurrentPosition(position: Int) =
        facade.insertCurrentPosition(CurrentPosition(position))

    override suspend fun updateCurrentPosition(pos: Int) = facade.updateCurrentPosition(pos)

    override suspend fun getWorkout(id: Int): Workout = facade.getWorkout(id)

    override fun toTime(seconds: Int): String {

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

    override fun buildCountDownTimer(tracks: List<Track>): Flow<Int> {
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

    override fun buildCountDownTimer(time: String): Flow<Int> {
        val timeList = time.split(":").map {
            it.toInt()
        }
        val totTime = timeList[0] * 3600 + timeList[1] * 60 + timeList[2]
        return flow<Int> {
            var currentTime = totTime
            while (currentTime >= 0) {
                delay(1000)
                currentTime--
                emit(currentTime)
            }
        }
    }
}