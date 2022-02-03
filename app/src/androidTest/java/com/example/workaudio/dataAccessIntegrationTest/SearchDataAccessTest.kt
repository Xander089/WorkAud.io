package com.example.workaudio.dataAccessIntegrationTest

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.workaudio.TestDataSource
import com.example.workaudio.TestDatabaseFactory
import com.example.workaudio.TestRetrofitFactory
import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.usecases.searchTracks.SearchDataAccess
import com.example.workaudio.data.database.ApplicationDAO
import com.example.workaudio.data.database.ApplicationDatabase
import com.example.workaudio.data.database.WorkoutRoomEntity
import com.example.workaudio.data.web.SpotifyRestApi
import com.example.workaudio.data.web.SpotifyWebService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SearchDataAccessTest {

    private lateinit var dataAccess: SearchDataAccess
    private lateinit var dao: ApplicationDAO
    private lateinit var source: TestDataSource
    private var id = 0

    @Before
    fun createDb() {

        source = TestDataSource()
        dao = TestDatabaseFactory.createDao()!!
        val webService = TestRetrofitFactory.createWebService()
        dataAccess = SearchDataAccess(dao, webService)

        runBlocking {
            dao.clearWorkouts()
            dao.insertWorkout(source.workoutRoomEntity)
            id = dao.getLatestWorkout()?.id ?: 0
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        TestDatabaseFactory.disposeDb()
    }

    @Test
    fun whenTracksSearchedFromWebApi_thenTheyAreReturned() = runBlocking(Dispatchers.Main) {
        //Given
        val queryText = "R"
        val token = dao.getToken()?.token.orEmpty()
        //When
        dataAccess.searchTracksAsObservable(queryText, token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Track>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: List<Track>) {
                    assertTrue(t.size == 20)
                }
                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            })
    }


    @Test
    fun whenTracksRequested_thenReturnedAsFLow() = runBlocking {
        //Given
        val track = source.tracks[0]
        //When
        dataAccess.insertTrack(track, id)
        //Then
        val resultTrack = dataAccess.getWorkoutTracksAsFlow(id).first()?.get(0)
        assertEquals(track.title, resultTrack?.title)

        dao.clearWorkoutsTracks()
    }

    @Test
    fun whenImageUrlUpdated_thenWorkoutImageUrlUpdatedAccordingly() = runBlocking {
        //When
        dataAccess.updateWorkoutDefaultImage("new", id)

        //Then
        val newUrl = dataAccess.getWorkoutAsFlow(id).first()?.imageUrl
        assertEquals("new", newUrl)

        dao.clearWorkouts()
    }

}