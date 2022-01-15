package com.example.workaudio.core.usecases.workoutList


import com.example.workaudio.core.EntityMapper.toTrack
import com.example.workaudio.core.EntityMapper.toWorkout
import com.example.workaudio.core.entities.Track
import com.example.workaudio.data.database.ApplicationDAO


import kotlinx.coroutines.flow.map

class ListDataAccess(
    private val dao: ApplicationDAO
) : ListDataAccessInterface{

    override fun getWorkouts() = dao.getAllWorkouts().map { workoutRoomEntities ->
        workoutRoomEntities.map { workoutRoomEntity ->
            toWorkout(workoutRoomEntity,emptyList<Track>())
        }
    }

    override suspend fun deleteWorkout(workoutId: Int) = dao.deleteWorkout(workoutId)
    override suspend fun getWorkoutTrack(workoutId: Int) = dao.getWorkoutTrack(workoutId).toTrack()



}