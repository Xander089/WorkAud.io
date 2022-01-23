package com.example.workaudio.core.usecases.detail

import com.example.workaudio.core.EntityMapper.toTrack
import com.example.workaudio.core.EntityMapper.toTrackRoomEntity
import com.example.workaudio.core.EntityMapper.toWorkout
import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.data.database.ApplicationDAO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DetailDataAccess(
    private val dao: ApplicationDAO
) : DetailDataAccessInterface {



    override fun getWorkoutAsFlow(workoutId: Int): Flow<Workout> =
        dao.getWorkoutById(workoutId).map {
            it.toWorkout(emptyList())
        }

    override fun getWorkoutTracksAsFlow(workoutId: Int): Flow<List<Track>> =
        dao.getWorkoutTracksFlow(workoutId).map {
            it.map { entity ->
                entity.toTrack()
            }
        }


    override suspend fun updateWorkoutName(name: String, id: Int) {
        dao.updateWorkoutName(name, id)
    }


    override suspend fun updateWorkoutDuration(id: Int, duration: Int) {
        dao.updateWorkoutDuration(id, duration)
    }



    override suspend fun deleteTrack(uri: String, id: Int) {
        dao.deleteWorkoutTrack(uri, id)
    }




}