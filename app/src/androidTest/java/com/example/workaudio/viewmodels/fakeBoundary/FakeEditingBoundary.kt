package com.example.workaudio.viewmodels.fakeBoundary

import com.example.workaudio.Constants
import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.core.usecases.detail.DetailBoundary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class FakeEditingBoundary: DetailBoundary {

    private val track = Track(
        "test_title",
        "test_uri",
        3 * Constants.MILLIS_IN_A_MINUTE,
        "test_artist",
        "test_album",
        "test_img_url",
        0
    )


    private val trackList = mutableListOf(track)

    private var workout: Workout = Workout(
        0,
        "test_workout",
        30 * Constants.MILLIS_IN_A_MINUTE,
        emptyList(),
        "test_url"
    )

    private val workouts = listOf(workout)


    override fun getWorkout(id: Int): Flow<Workout> {
       return flow<Workout> {
          val w =  workouts.find {
               it.id == 0
           }
           emit(w!!)
       }
    }

    override suspend fun getWorkout(): Workout {
        return workout
    }

    override fun getWorkoutTracks(workoutId: Int): Flow<List<Track>> {
        return flow { emit(trackList) }
    }

    override suspend fun updateWorkoutName(id: Int, name: String) {
        val w = workouts.find { it.id == id }
        w!!.name = name
    }

    override suspend fun updateWorkoutDuration(id: Int, duration: Int) {
        val w = workouts.find { it.id == id }
        w!!.duration = duration
    }

    override suspend fun insertWorkoutTrack(id: Int, track: Track) {
        trackList.add(track)
    }

    override suspend fun deleteTrack(uri: String, id: Int) {
        trackList.removeIf {
            it.uri == uri
        }
    }
}