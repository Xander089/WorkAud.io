package com.example.workaudio.core.usecases.player

import com.example.workaudio.core.EntityMapper.toTrack
import com.example.workaudio.core.EntityMapper.toWorkout
import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.repository.database.ApplicationDAO
import com.example.workaudio.repository.database.CurrentPosition
import com.example.workaudio.repository.database.WorkoutRoomEntity
import com.example.workaudio.repository.database.WorkoutTracksRoomEntity

class PlayerDataAccess(
    private val dao: ApplicationDAO
) : PlayerDataAccessInterface{

    override fun getCurrentPosition() = dao.getCurrentPosition()
    override suspend fun clearCurrentPosition() = dao.clearCurrentPosition()
    override suspend fun insertCurrentPosition(position: CurrentPosition) = dao.insertCurrentPosition(position)
    override suspend fun updateCurrentPosition(pos: Int) = dao.updateCurrentPosition(pos)

    override suspend fun getWorkout(id: Int): Workout {
        val tracksRoomEntity = dao.getWorkoutTracks(id)
        val workoutRoomEntity = dao.getWorkout(id)
        val tracks = tracksRoomEntity.map { trackEntity ->
            trackEntity.toTrack()
        }
        return toWorkout(workoutRoomEntity,tracks)
    }

}