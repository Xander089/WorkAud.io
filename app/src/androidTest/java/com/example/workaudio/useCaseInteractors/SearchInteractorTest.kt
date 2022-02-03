package com.example.workaudio.useCaseInteractors

import com.example.workaudio.TestDataSource
import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.usecases.searchTracks.SearchDataAccessInterface
import com.example.workaudio.core.usecases.searchTracks.SearchInteractor
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SearchInteractorTest {

    private lateinit var searchInteractor: SearchInteractor

    @Mock
    lateinit var dataAccess: SearchDataAccessInterface

    private lateinit var source: TestDataSource

    @Before
    fun setUp() {
        source = TestDataSource()
        searchInteractor = SearchInteractor(dataAccess)
    }


    @Test
    fun whenWorkoutRequestedById_thenItIsReturned() = runBlocking {
        //Given
        val id = 0
        val expected = source.workout.name
        //When
        Mockito.`when`(dataAccess.getWorkoutAsFlow(id))
            .thenReturn(flow { emit(source.workout) })

        val workoutName = searchInteractor.getWorkout(id).first()?.name.orEmpty()
        //Then

        assertEquals(expected, workoutName)
    }

    @Test
    fun whenWorkoutTracksRequestedById_thenTheyAreReturned() = runBlocking {
        //Given
        val id = 0
        val expected = source.tracks[0].title
        //When
        Mockito.`when`(dataAccess.getWorkoutTracksAsFlow(id))
            .thenReturn(flow { emit(source.tracks) })

        val actual = searchInteractor.getWorkoutTracks(id).first()?.get(0)?.title
        //Then

        assertEquals(expected, actual)
    }

    @Test
    fun whenTracksAreSearched_thenTheyAreReturnedAsRXJava3Observable() = runBlocking {
        //GIVEN
        val expected = "title1"
        val query = "query"
        //WHEN
        Mockito.`when`(
            dataAccess.searchTracksAsObservable(query, "")
        ).thenReturn(Observable.just(source.tracks))

        //Then
        searchInteractor.searchTracksAsObservable(query, "")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Track>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: List<Track>) {
                    assertEquals(expected, t[0].title)
                }

                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            })
    }

    @Test
    fun whenTrackIsAdded_thenTrackListSizeIncreaseByOne() = runBlocking {
        //Given
        val expected = source.tracks[0]
        val id = 0
        //When
        Mockito.`when`(dataAccess.insertTrack(expected, id))
            .thenReturn(addTrack(expected))
        //Then
        searchInteractor.addTrack(expected, id)
        assertEquals(expected, source.tracks.last())

    }

    private fun addTrack(track: Track) {
        source.tracks.add(track)
    }

    @Test
    fun whenWorkoutDefaultImageIsUpdatedAs_newImageUrl_thenItIsReturnedAs_newImageUrl() =
        runBlocking {
            //Given
            val id = 0
            val expected = "newImageUrl"
            //When
            Mockito.`when`(dataAccess.updateWorkoutDefaultImage(expected, id))
                .thenReturn(update(expected))
            //Then
            searchInteractor.updateWorkoutDefaultImage(expected, id)
            assertEquals(expected, source.workout.imageUrl)


        }

    private fun update(url: String) {
        source.workout.imageUrl = url
    }
}