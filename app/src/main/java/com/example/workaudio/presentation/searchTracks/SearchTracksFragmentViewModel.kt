package com.example.workaudio.presentation.searchTracks

import androidx.lifecycle.*
import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.core.usecases.searchTracks.SearchBoundary
import com.example.workaudio.common.Constants.MILLISECONDS_IN_A_MINUTE
import com.example.workaudio.common.Constants.MIN
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchTracksFragmentViewModel @Inject constructor(private val searchInteractor: SearchBoundary) :
    ViewModel() {

    private var dispatcher: CoroutineDispatcher = Dispatchers.IO
    fun setDispatcher(dispatcher: CoroutineDispatcher) {
        this.dispatcher = dispatcher
    }

    private lateinit var searchTracksObservable: Observable<List<Track>>

    //the cached spotify auth token: needed to make http get requests towards spotify web API
    private val _token = MutableLiveData<String>()
    val token: LiveData<String> = _token

    private fun readToken() =
        viewModelScope.launch {
            _token.value = searchInteractor.getToken()
        }


    private val _searchedTracks = MutableLiveData<List<Track>>()
    val searchedTracks: LiveData<List<Track>> = _searchedTracks
    var currentWorkout: LiveData<Workout?> = MutableLiveData<Workout?>()
    var workoutTracks: LiveData<List<Track>?> = MutableLiveData()

    fun setupCurrentWorkout(workoutId: Int) {
        readToken()
        currentWorkout = searchInteractor.getWorkout(workoutId).asLiveData()
        workoutTracks = searchInteractor.getWorkoutTracks(workoutId).asLiveData()
    }

    //when user taps add track in rec view -> cache the track
    fun addTrack(track: Track, workoutId: Int) {
        viewModelScope.launch(dispatcher) {
            searchInteractor.addTrack(track, workoutId)
        }
    }

    //uses rxjava Observable to async fetch tracks form spotify web api
    fun searchTracksAsObservable(
        query: String,
        refreshUIList: (List<Track>) -> Unit = {}
    ) {
        if (query.isEmpty()) {
            return
        }

        searchTracksObservable = searchInteractor
            .searchTracksAsObservable(
                query,
                token.value.orEmpty()
            )

        searchTracksObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Track>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: List<Track>) =
                    refreshUIList(t)
                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            })
    }

    //whenever user adds a new tracks -> update the workout default image url
    fun updateWorkoutDefaultImage(imageUrl: String, workoutId: Int) {
        viewModelScope.launch(dispatcher) {
            searchInteractor.updateWorkoutDefaultImage(imageUrl, workoutId)
        }
    }


    /* Helper methods to format ui objects */

    fun updateProgressBar(tracks: List<Track>): Int {
        val target = targetDuration.toFloat()
        val current = tracks.map { it.duration }.sum().toFloat()
        return (current / target * 100).toInt()
    }

    fun formatCurrentDuration(tracks: List<Track>): String =
        tracks.map { it.duration / MILLISECONDS_IN_A_MINUTE }.sum().toString()


    fun formatDuration(duration: Int): String =
        "/${(duration / MILLISECONDS_IN_A_MINUTE)}$MIN"


    fun formatSnackBarText(title: String, decoration: String) = "$title $decoration"

    /**/
    private var targetDuration = -1
    fun setTargetDuration(workout: Workout) {
        targetDuration = workout.duration
    }

    private var scrollState = 0
    fun setScrollState(state: Int) {
        scrollState = state
    }

    fun getScrollState() = scrollState


}