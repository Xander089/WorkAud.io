package com.example.workaudio.useCaseInteractors.list

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.core.usecases.searchTracks.SearchDataAccessInterface
import com.example.workaudio.core.usecases.workoutList.ListDataAccess
import com.example.workaudio.core.usecases.workoutList.ListDataAccessInterface
import com.example.workaudio.data.database.WorkoutRoomEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeListDataAccess : ListDataAccessInterface {

    private val workout = Workout(
        name = "test_name",
        duration = 30 * 1000 * 60,
        imageUrl = "test",
        tracks = emptyList(),
        id = 0
    )
    private val workoutList = mutableListOf(workout)
    private val track = Track("title", "uri", 1000, "artist", "album", "url", 0)
    private val trackList = mutableListOf(track)


    override fun getWorkouts(): Flow<List<Workout>> {
        return flow { emit(workoutList) }
    }

    override suspend fun deleteWorkout(workoutId: Int) {
        workoutList.removeIf {
            it.id == workoutId
        }
    }

    override suspend fun getWorkoutTrack(workoutId: Int): Track {
        return trackList.first()
    }


}