package com.example.workaudio.core.usecases.creation

import com.example.workaudio.core.EntityMapper
import com.example.workaudio.core.EntityMapper.toTrack
import com.example.workaudio.core.EntityMapper.toTrackRoomEntity
import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.repository.database.WorkoutRoomEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface CreationDataAccessInterface {

    fun getLatestWorkoutAsFlow(): Flow<Workout?>
    suspend fun searchTracks(queryText: String): List<Track>

    suspend fun getWorkout(): Workout?


    suspend fun insertWorkout(
        name: String,
        duration: Int,
        tracks: List<Track>,
    )


    suspend fun insertWorkout(
        name: String,
        duration: Int,
    )

    suspend fun insertWorkoutTracks(tracks: List<Track>)
}