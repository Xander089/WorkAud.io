package com.example.workaudio.core.usecases.creation

import com.example.workaudio.core.EntityMapper.toTrack
import com.example.workaudio.core.EntityMapper.toTrackRoomEntity
import com.example.workaudio.core.EntityMapper.toWorkout
import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.data.database.ApplicationDAO
import com.example.workaudio.data.database.WorkoutRoomEntity
import com.example.workaudio.data.web.SpotifyWebService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map

class CreationDataAccess(
    private val dao: ApplicationDAO
) : CreationDataAccessInterface {


    override suspend fun insertWorkout(
        name: String,
        duration: Int
    ) {
        val workoutRoomEntity = WorkoutRoomEntity(name, duration, null)
        dao.insertWorkout(workoutRoomEntity)
    }


}