package com.example.workaudio.usecases.workoutCreation

import com.example.workaudio.entities.Track
import com.example.workaudio.entities.Workout

class WorkoutCreation {

    fun createWorkout(
        name: String,
        duration: Int,
        tracks: List<Track>
    ): Workout = Workout(name, duration, tracks)


}