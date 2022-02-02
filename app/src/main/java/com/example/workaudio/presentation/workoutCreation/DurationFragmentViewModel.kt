package com.example.workaudio.presentation.workoutCreation

import androidx.lifecycle.*
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.core.usecases.creation.CreationBoundary
import com.example.workaudio.common.Constants.MILLIS_IN_A_MINUTE
import com.example.workaudio.common.DataHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DurationFragmentViewModel @Inject constructor(private val useCaseBoundary: CreationBoundary) :
    ViewModel() {

    companion object {
        enum class STATE { CREATED, NOT_CREATED }
    }

    private var dispatcher: CoroutineDispatcher = Dispatchers.IO

    fun setDispatcher(dispatcher: CoroutineDispatcher){
        this.dispatcher = dispatcher
    }

    var state = STATE.NOT_CREATED
    var workout: LiveData<Workout?> = useCaseBoundary.getLatestWorkout().asLiveData()
    fun isWorkoutCreated() = state == STATE.CREATED
    fun formatDuration(duration: Float): String = DataHelper.formatDuration(duration)

    fun insertWorkout(
        name: String,
        duration: Int
    ) {
        this.state = STATE.CREATED
        viewModelScope.launch(dispatcher) {
            val durationInMillis = duration * MILLIS_IN_A_MINUTE
            useCaseBoundary.createWorkout(
                name,
                durationInMillis
            )
        }
    }
}