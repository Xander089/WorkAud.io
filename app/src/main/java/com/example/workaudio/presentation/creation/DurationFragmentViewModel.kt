package com.example.workaudio.presentation.creation

import androidx.lifecycle.*
import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.core.usecases.creation.CreationServiceBoundary
import com.example.workaudio.core.usecases.creation.WorkoutCreationInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DurationFragmentViewModel @Inject constructor(private val _workoutCreationInteractor: WorkoutCreationInteractor) :
    ViewModel() {

    companion object {
        private const val MIN = " min"
        enum class STATE{CREATED,NOT_CREATED}
    }

    private val workoutCreationInteractor: CreationServiceBoundary
    var state = STATE.NOT_CREATED

    init {
        workoutCreationInteractor = _workoutCreationInteractor
    }

    var workout: LiveData<Workout> = workoutCreationInteractor.latestWorkout.asLiveData()

    fun isWorkoutCreated() = state == STATE.CREATED

    fun formatDuration(duration: Float): String {
        val intDuration = duration.toInt()
        return "$intDuration$MIN"
    }

    fun insertWorkout(
        name: String,
        duration: Int
    ) {
        this.state = STATE.CREATED
        viewModelScope.launch(Dispatchers.IO) {
            val durationInMillis = duration * 60 * 1000
            workoutCreationInteractor.createWorkout(
                name,
                durationInMillis
            )
        }
    }
}