package com.example.workaudio.core.usecases.editing

import com.example.workaudio.core.EntityMapper.toTrack
import com.example.workaudio.core.EntityMapper.toTrackRoomEntity
import com.example.workaudio.core.EntityMapper.toWorkout
import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.data.database.ApplicationDAO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EditingDataAccess(
    private val dao: ApplicationDAO
) : EditingDataAccessInterface {

    override fun getWorkoutTracks(workoutId: Int) = dao.getWorkoutTracksFlow(workoutId).map {
        it.map { trackRoomEntity ->
            trackRoomEntity.toTrack()
        }
    }

    override fun getWorkoutAsFlow(workoutId: Int): Flow<Workout> = dao.getWorkoutById(workoutId).map {
        toWorkout(it, emptyList())
    }

    override  fun getWorkoutTracksAsFlow(workoutId: Int): Flow<List<Track>> =
        dao.getWorkoutTracksFlow(workoutId).map {
            it.map { entity ->
                entity.toTrack()
            }
        }


    override  suspend fun updateWorkoutName(name: String, id: Int) {
        dao.updateWorkoutName(name, id)
    }


    override suspend fun updateWorkoutDuration(id: Int, duration: Int) {
        dao.updateWorkoutDuration(id, duration)
    }

    override suspend fun insertWorkoutTrack(track: Track, id: Int) {
        dao.insertWorkoutTrack(
            track.toTrackRoomEntity(id)
        )
    }

    override  suspend fun deleteTrack(uri: String, id: Int) {
        dao.deleteWorkoutTrack(uri, id)
    }

    override suspend fun getWorkout(id: Int): Workout {
        val tracksRoomEntity = dao.getWorkoutTracks(id)
        val workoutRoomEntity = dao.getWorkout(id)
        val tracks = tracksRoomEntity.map { trackEntity ->
            trackEntity.toTrack()
        }
        return toWorkout(workoutRoomEntity,tracks)
    }

    override suspend fun getWorkout(): Workout {
        val workoutRoomEntity = dao.getLatestWorkout()
        val tracksRoomEntity = dao.getWorkoutTracks(workoutRoomEntity.id)
        val tracks = tracksRoomEntity.map { trackEntity ->
            trackEntity.toTrack()
        }
        return toWorkout(workoutRoomEntity,tracks)
    }
}