package com.example.workaudio.usecases.workoutNavigation

import com.example.workaudio.entities.Track
import com.example.workaudio.entities.Workout
import com.example.workaudio.repository.database.ApplicationDAO
import com.example.workaudio.repository.database.WorkoutRoomEntity
import com.example.workaudio.repository.database.WorkoutTracksRoomEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

class WorkoutNavigationFacade(
    private val dao: ApplicationDAO
) {

    suspend fun getAllWorkouts(): List<Workout> {
        val flow = dao.getAllWorkouts()
        val workoutList = mutableListOf<Workout>()
        flow.collect { list ->
            list.forEach { entity ->
                workoutList.add(entity.toWorkout(emptyList<Track>()))
            }
        }
        return workoutList
    }

    suspend fun getWorkout(id: Int): Workout? {
        val tracksFlow = dao.getWorkoutTracks(id)
        val workoutFlow = dao.getWorkout(id)
        val trackList = mutableListOf<Track>()
        var workout: Workout? = null

        tracksFlow.collect {
            it.forEach { trackEntity ->
                trackList.add(trackEntity.toTrack())
            }
            workoutFlow.collect { entity ->
                workout = entity.toWorkout(trackList)
            }
        }
        return workout
    }

    private fun WorkoutRoomEntity.toWorkout(
        tracks: List<Track>
    ): Workout {
        return Workout(
            this.id,
            this.name.orEmpty(),
            this.currentDuration ?: 0,
            this.duration ?: 0,
            tracks
        )
    }

    private fun WorkoutTracksRoomEntity.toTrack(): Track {
        return Track(
            this.title.orEmpty(),
            this.uri.orEmpty(),
            this.duration ?: 0,
            this.artist.orEmpty(),
            this.album.orEmpty(),
            this.imageUrl.orEmpty()
        )
    }

}