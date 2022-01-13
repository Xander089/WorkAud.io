package com.example.workaudio.presentation.creation

import androidx.lifecycle.*
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.core.usecases.creation.CreationServiceBoundary
import com.example.workaudio.Constants.MILLIS_IN_A_MINUTE
import com.example.workaudio.Constants.MIN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DurationFragmentViewModel @Inject constructor(private val useCaseBoundary: CreationServiceBoundary) :
    ViewModel() {

    companion object {
        enum class STATE{CREATED,NOT_CREATED}
    }

    var state = STATE.NOT_CREATED

    var workout: LiveData<Workout?> = useCaseBoundary.getLatestWorkout().asLiveData()

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
            val durationInMillis = duration * MILLIS_IN_A_MINUTE
            useCaseBoundary.createWorkout(
                name,
                durationInMillis
            )
        }
    }
}