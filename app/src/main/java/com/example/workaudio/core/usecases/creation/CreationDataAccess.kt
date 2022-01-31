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
    private val dao: ApplicationDAO,
    private val service: SpotifyWebService,
) : CreationDataAccessInterface {

    override suspend fun searchTracks(queryText: String): List<Track> {
        val token = dao.getToken()?.token.orEmpty()
        val gsonResponse = service.fetchTracks(token, queryText)
        return gsonResponse.tracks.items.map { it.toTrack() }
    }

    //when database entity is empty -> return null
    override suspend fun getWorkout(): Workout? = dao.getLatestWorkout()?.toWorkout()

    //when database entity is empty -> map flow to null
    override fun getLatestWorkoutAsFlow() = dao.getLatestWorkoutAsFlow().map {
        it?.toWorkout()
    }

    override suspend fun insertWorkout(
        name: String,
        duration: Int,
        tracks: List<Track>,
    ) {
        val imageUrl = tracks[0].imageUrl
        val workoutRoomEntity = WorkoutRoomEntity(name, duration, imageUrl)
        dao.insertWorkout(workoutRoomEntity)
        delay(500)
        insertWorkoutTracks(tracks)
    }

    override suspend fun insertWorkout(
        name: String,
        duration: Int,
    ) {
        val workoutRoomEntity = WorkoutRoomEntity(name, duration, null)
        dao.insertWorkout(workoutRoomEntity)
    }

    override suspend fun insertWorkoutTracks(tracks: List<Track>) {
        dao.getLatestWorkout()?.id?.let { id ->
            tracks.forEach { track ->
                val roomEntityTrack = track.toTrackRoomEntity(id)
                dao.insertWorkoutTrack(roomEntityTrack)
            }
        }
    }


}