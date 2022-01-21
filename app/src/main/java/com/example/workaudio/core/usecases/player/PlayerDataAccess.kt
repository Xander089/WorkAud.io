package com.example.workaudio.core.usecases.player

import com.example.workaudio.core.EntityMapper.toTrack
import com.example.workaudio.core.EntityMapper.toWorkout
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.data.database.ApplicationDAO

class PlayerDataAccess(
    private val dao: ApplicationDAO
) : PlayerDataAccessInterface{


    override suspend fun getWorkout(id: Int): Workout {
        val tracksRoomEntity = dao.getWorkoutTracks(id)
        val workoutRoomEntity = dao.getWorkout(id)
        val tracks = tracksRoomEntity.map { trackEntity ->
            trackEntity.toTrack()
        }
        return workoutRoomEntity.toWorkout(tracks)
    }

}